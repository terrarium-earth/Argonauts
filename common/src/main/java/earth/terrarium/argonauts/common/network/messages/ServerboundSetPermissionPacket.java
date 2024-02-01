package earth.terrarium.argonauts.common.network.messages;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.guild.GuildApi;
import earth.terrarium.argonauts.api.party.PartyApi;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.handlers.GroupType;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.base.members.Group;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;
import java.util.function.Consumer;

public record ServerboundSetPermissionPacket(String permission,
                                             boolean value, GroupType group,
                                             UUID member) implements Packet<ServerboundSetPermissionPacket> {

    public static final ServerboundPacketType<ServerboundSetPermissionPacket> TYPE = new Type();

    @Override
    public PacketType<ServerboundSetPermissionPacket> type() {
        return TYPE;
    }

    private static class Type extends CodecPacketType<ServerboundSetPermissionPacket> implements ServerboundPacketType<ServerboundSetPermissionPacket> {

        public Type() {
            super(
                ServerboundSetPermissionPacket.class,
                new ResourceLocation(Argonauts.MOD_ID, "set_permission"),
                ObjectByteCodec.create(
                    ByteCodec.STRING.fieldOf(ServerboundSetPermissionPacket::permission),
                    ByteCodec.BOOLEAN.fieldOf(ServerboundSetPermissionPacket::value),
                    ByteCodec.ofEnum(GroupType.class).fieldOf(ServerboundSetPermissionPacket::group),
                    ByteCodec.UUID.fieldOf(ServerboundSetPermissionPacket::member),
                    ServerboundSetPermissionPacket::new
                )
            );
        }

        @Override
        public Consumer<Player> handle(ServerboundSetPermissionPacket packet) {
            return player ->
                CommandHelper.runNetworkAction(player, () -> {
                    Group<?, ?> group = null;
                    if (packet.group == GroupType.GUILD) {
                        group = GuildApi.API.get((ServerPlayer) player);
                    } else if (packet.group == GroupType.PARTY) {
                        group = PartyApi.API.get(player);
                    }
                    if (group == null) return;

                    Member member = group.getMember(player);
                    if (!member.hasPermission(MemberPermissions.MANAGE_PERMISSIONS)) {
                        throw MemberException.NO_PERMISSIONS;
                    }
                    Member selected = group.getMember(packet.member());
                    if (selected != null && !selected.getState().isLeader()) {
                        if (!member.hasPermission(packet.permission)) {
                            throw MemberException.YOU_CANT_GIVE_PERMISSIONS;
                        }
                        if (packet.value) {
                            selected.permissions().add(packet.permission);
                        } else {
                            selected.permissions().remove(packet.permission);
                        }
                    }
                });
        }
    }
}
