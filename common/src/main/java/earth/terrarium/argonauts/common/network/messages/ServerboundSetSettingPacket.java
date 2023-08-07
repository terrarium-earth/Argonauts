package earth.terrarium.argonauts.common.network.messages;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.party.Party;
import earth.terrarium.argonauts.api.party.PartyApi;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.party.members.PartyMember;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record ServerboundSetSettingPacket(String setting, boolean value,
                                          UUID member, boolean partySettings) implements Packet<ServerboundSetSettingPacket> {

    public static final ResourceLocation ID = new ResourceLocation(Argonauts.MOD_ID, "set_setting");
    public static final PacketHandler<ServerboundSetSettingPacket> HANDLER = new Handler();

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<ServerboundSetSettingPacket> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<ServerboundSetSettingPacket> {

        @Override
        public void encode(ServerboundSetSettingPacket message, FriendlyByteBuf buffer) {
            buffer.writeUtf(message.setting);
            buffer.writeBoolean(message.value);
            buffer.writeUUID(message.member);
            buffer.writeBoolean(message.partySettings);
        }

        @Override
        public ServerboundSetSettingPacket decode(FriendlyByteBuf buffer) {
            return new ServerboundSetSettingPacket(
                buffer.readUtf(),
                buffer.readBoolean(),
                buffer.readUUID(),
                buffer.readBoolean());
        }

        @Override
        public PacketContext handle(ServerboundSetSettingPacket message) {
            return (player, level) ->
                CommandHelper.runNetworkAction(player, () -> {
                    Party party = PartyApi.API.getPlayerParty(message.member());
                    if (party == null) return;

                    PartyMember member = party.getMember(message.member());
                    if (!member.hasPermission(MemberPermissions.MANAGE_SETTINGS)) {
                        throw MemberException.NO_PERMISSIONS;
                    }
                    if (message.partySettings()) {
                        party.settings().set(message.setting, message.value);
                    } else {
                        member.settings().set(message.setting, message.value);
                    }
                });
        }
    }
}
