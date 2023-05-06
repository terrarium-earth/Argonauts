package earth.terrarium.argonauts.common.network.messages;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.party.Party;
import earth.terrarium.argonauts.common.handlers.party.PartyHandler;
import earth.terrarium.argonauts.common.handlers.party.members.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.party.members.PartyMember;
import earth.terrarium.argonauts.common.menus.party.PartyMembersMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ServerboundSetRolePacket(String role) implements Packet<ServerboundSetRolePacket> {

    public static final ResourceLocation ID = new ResourceLocation(Argonauts.MOD_ID, "set_role");
    public static final PacketHandler<ServerboundSetRolePacket> HANDLER = new Handler();

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<ServerboundSetRolePacket> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<ServerboundSetRolePacket> {

        @Override
        public void encode(ServerboundSetRolePacket message, FriendlyByteBuf buffer) {
            buffer.writeUtf(message.role);
        }

        @Override
        public ServerboundSetRolePacket decode(FriendlyByteBuf buffer) {
            return new ServerboundSetRolePacket(buffer.readUtf());
        }

        @Override
        public PacketContext handle(ServerboundSetRolePacket message) {
            return (player, level) ->
                CommandHelper.runNetworkAction(player, () -> {
                    Party party = PartyHandler.get(player);
                    if (player.containerMenu instanceof PartyMembersMenu menu && party != null) {
                        PartyMember member = party.getMember(player);
                        if (!member.hasPermission(MemberPermissions.MANAGE_ROLES)) {
                            throw MemberException.NO_PERMISSIONS;
                        }
                        PartyMember selected = menu.getSelected();
                        if (selected != null && !selected.getState().isLeader()) {
                            selected.setRole(message.role);
                        }
                    }
                });
        }
    }
}
