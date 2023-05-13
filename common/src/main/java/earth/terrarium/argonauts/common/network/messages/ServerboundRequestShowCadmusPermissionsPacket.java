package earth.terrarium.argonauts.common.network.messages;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.common.handlers.base.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import earth.terrarium.argonauts.common.handlers.guild.Guild;
import earth.terrarium.argonauts.common.handlers.guild.GuildHandler;
import earth.terrarium.argonauts.common.handlers.party.Party;
import earth.terrarium.argonauts.common.handlers.party.PartyHandler;
import earth.terrarium.argonauts.common.network.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ServerboundRequestShowCadmusPermissionsPacket() implements Packet<ServerboundRequestShowCadmusPermissionsPacket> {

    public static final ResourceLocation ID = new ResourceLocation(Argonauts.MOD_ID, "request_show_cadmus_permissions");
    public static final Handler HANDLER = new Handler();

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<ServerboundRequestShowCadmusPermissionsPacket> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<ServerboundRequestShowCadmusPermissionsPacket> {

        @Override
        public void encode(ServerboundRequestShowCadmusPermissionsPacket message, FriendlyByteBuf buffer) {
        }

        @Override
        public ServerboundRequestShowCadmusPermissionsPacket decode(FriendlyByteBuf buffer) {
            return new ServerboundRequestShowCadmusPermissionsPacket();
        }

        @Override
        public PacketContext handle(ServerboundRequestShowCadmusPermissionsPacket message) {
            return (player, level) -> {
                Party party = PartyHandler.getPlayerParty(player.getUUID());
                if (party == null) return;
                if (!party.members().get(player.getUUID()).getState().isLeader()) return;
                Guild guild = GuildHandler.getPlayerGuild(player.getServer(), player.getUUID());
                if (guild == null) return;
                Member member = guild.members().get(player.getUUID());
                if (member.hasPermission(MemberPermissions.TEMPORARY_GUILD_PERMISSIONS) || member.getState().isLeader()) {
                    NetworkHandler.CHANNEL.sendToPlayer(new ClientboundReceiveShowCadmusPermissionsPacket(), player);
                }
            };
        }
    }
}
