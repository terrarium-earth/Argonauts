package earth.terrarium.argonauts.common.network.messages;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.guild.GuildApi;
import earth.terrarium.argonauts.api.party.PartyApi;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.handlers.GroupType;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.base.members.Group;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public record ServerboundSetRolePacket(String role, GroupType type,
                                       UUID member) implements Packet<ServerboundSetRolePacket> {

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
            buffer.writeEnum(message.type);
            buffer.writeUUID(message.member);
        }

        @Override
        public ServerboundSetRolePacket decode(FriendlyByteBuf buffer) {
            return new ServerboundSetRolePacket(
                buffer.readUtf(),
                buffer.readEnum(GroupType.class),
                buffer.readUUID());
        }

        @Override
        public PacketContext handle(ServerboundSetRolePacket message) {
            return (player, level) ->
                CommandHelper.runNetworkAction(player, () -> {
                    Group<?, ?> group = null;
                    if (message.type == GroupType.GUILD) {
                        group = GuildApi.API.get((ServerPlayer) player);
                    } else if (message.type == GroupType.PARTY) {
                        group = PartyApi.API.get(player);
                    }
                    if (group == null) return;

                    Member member = group.getMember(player);
                    if (!member.hasPermission(MemberPermissions.MANAGE_ROLES)) {
                        throw MemberException.NO_PERMISSIONS;
                    }
                    Member selected = group.members().get(message.member);
                    if (selected != null && !selected.getState().isLeader()) {
                        selected.setRole(message.role);
                    }
                });
        }
    }
}
