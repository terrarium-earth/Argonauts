package earth.terrarium.argonauts.common.network.messages;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.party.Party;
import earth.terrarium.argonauts.api.party.PartyApi;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.party.members.PartyMember;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;
import java.util.function.Consumer;

public record ServerboundSetSettingPacket(String setting, boolean value,
                                          UUID member,
                                          boolean partySettings) implements Packet<ServerboundSetSettingPacket> {

    public static final ServerboundPacketType<ServerboundSetSettingPacket> TYPE = new Type();

    @Override
    public PacketType<ServerboundSetSettingPacket> type() {
        return TYPE;
    }

    private static class Type extends CodecPacketType<ServerboundSetSettingPacket> implements ServerboundPacketType<ServerboundSetSettingPacket> {

        public Type() {
            super(
                ServerboundSetSettingPacket.class,
                new ResourceLocation(Argonauts.MOD_ID, "set_setting"),
                ObjectByteCodec.create(
                    ByteCodec.STRING.fieldOf(ServerboundSetSettingPacket::setting),
                    ByteCodec.BOOLEAN.fieldOf(ServerboundSetSettingPacket::value),
                    ByteCodec.UUID.fieldOf(ServerboundSetSettingPacket::member),
                    ByteCodec.BOOLEAN.fieldOf(ServerboundSetSettingPacket::partySettings),
                    ServerboundSetSettingPacket::new
                )
            );
        }

        @Override
        public Consumer<Player> handle(ServerboundSetSettingPacket packet) {
            return player ->
                CommandHelper.runNetworkAction(player, () -> {
                    Party party = PartyApi.API.getPlayerParty(packet.member());
                    if (party == null) return;

                    PartyMember member = party.getMember(packet.member());
                    if (!member.hasPermission(MemberPermissions.MANAGE_SETTINGS)) {
                        throw MemberException.NO_PERMISSIONS;
                    }
                    if (packet.partySettings()) {
                        party.settings().set(packet.setting, packet.value);
                    } else {
                        member.settings().set(packet.setting, packet.value);
                    }
                });
        }
    }
}
