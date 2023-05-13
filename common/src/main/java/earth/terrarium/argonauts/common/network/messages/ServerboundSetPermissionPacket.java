package earth.terrarium.argonauts.common.network.messages;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.base.members.Group;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import earth.terrarium.argonauts.common.handlers.guild.GuildHandler;
import earth.terrarium.argonauts.common.handlers.party.PartyHandler;
import earth.terrarium.argonauts.common.menus.base.MembersMenu;
import earth.terrarium.argonauts.common.menus.guild.GuildMembersMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public record ServerboundSetPermissionPacket(String permission,
                                             boolean value) implements Packet<ServerboundSetPermissionPacket> {

    public static final ResourceLocation ID = new ResourceLocation(Argonauts.MOD_ID, "set_permission");
    public static final PacketHandler<ServerboundSetPermissionPacket> HANDLER = new Handler();

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<ServerboundSetPermissionPacket> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<ServerboundSetPermissionPacket> {

        @Override
        public void encode(ServerboundSetPermissionPacket message, FriendlyByteBuf buffer) {
            buffer.writeUtf(message.permission);
            buffer.writeBoolean(message.value);
        }

        @Override
        public ServerboundSetPermissionPacket decode(FriendlyByteBuf buffer) {
            return new ServerboundSetPermissionPacket(buffer.readUtf(), buffer.readBoolean());
        }

        @Override
        public PacketContext handle(ServerboundSetPermissionPacket message) {
            return (player, level) ->
                CommandHelper.runNetworkAction(player, () -> {
                    Group<?> group = null;
                    if (player.containerMenu instanceof GuildMembersMenu) {
                        group = GuildHandler.get((ServerPlayer) player);
                    } else if (player.containerMenu instanceof MembersMenu) {
                        group = PartyHandler.get(player);
                    }

                    if (player.containerMenu instanceof MembersMenu menu && group != null) {
                        Member member = group.getMember(player);
                        if (!member.hasPermission(MemberPermissions.MANAGE_PERMISSIONS)) {
                            throw MemberException.NO_PERMISSIONS;
                        }
                        Member selected = menu.getSelected();
                        if (selected != null && !selected.getState().isLeader()) {
                            if (!member.hasPermission(message.permission)) {
                                throw MemberException.YOU_CANT_GIVE_PERMISSIONS;
                            }
                            if (message.value) {
                                selected.permissions().add(message.permission);
                            } else {
                                selected.permissions().remove(message.permission);
                            }
                        }
                    }
                });
        }
    }
}
