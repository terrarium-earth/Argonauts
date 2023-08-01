package earth.terrarium.argonauts.common.network.messages;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.guild.Guild;
import earth.terrarium.argonauts.api.guild.GuildApi;
import earth.terrarium.argonauts.api.party.Party;
import earth.terrarium.argonauts.api.party.PartyApi;
import earth.terrarium.argonauts.common.handlers.base.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
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
                Party party = PartyApi.API.getPlayerParty(player.getUUID());
                if (party == null) return;
                if (!party.members().get(player.getUUID()).getState().isLeader()) return;
                Guild guild = GuildApi.API.getPlayerGuild(player.getServer(), player.getUUID());
                if (guild == null) return;
                Member member = guild.members().get(player.getUUID());
                if (!member.hasPermission(MemberPermissions.TEMPORARY_GUILD_PERMISSIONS) && !member.getState().isLeader()) {
                    return;
                }
                if (!NetworkHandler.CHANNEL.canSendPlayerPackets(player)) return;
                NetworkHandler.CHANNEL.sendToPlayer(new ClientboundReceiveShowCadmusPermissionsPacket(), player);
            };
        }
    }
}
