package earth.terrarium.argonauts.common.network.messages;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
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
import net.minecraft.world.entity.player.Player;

import java.util.function.Consumer;

public record ServerboundRequestShowCadmusPermissionsPacket() implements Packet<ServerboundRequestShowCadmusPermissionsPacket> {

    public static final ServerboundPacketType<ServerboundRequestShowCadmusPermissionsPacket> TYPE = new Type();

    @Override
    public PacketType<ServerboundRequestShowCadmusPermissionsPacket> type() {
        return TYPE;
    }

    private static class Type implements ServerboundPacketType<ServerboundRequestShowCadmusPermissionsPacket> {

        @Override
        public Class<ServerboundRequestShowCadmusPermissionsPacket> type() {
            return ServerboundRequestShowCadmusPermissionsPacket.class;
        }

        @Override
        public ResourceLocation id() {
            return new ResourceLocation(Argonauts.MOD_ID, "request_show_cadmus_permissions");
        }

        @Override
        public void encode(ServerboundRequestShowCadmusPermissionsPacket message, FriendlyByteBuf buffer) {}

        @Override
        public ServerboundRequestShowCadmusPermissionsPacket decode(FriendlyByteBuf buffer) {
            return new ServerboundRequestShowCadmusPermissionsPacket();
        }

        @Override
        public Consumer<Player> handle(ServerboundRequestShowCadmusPermissionsPacket packet) {
            return player -> {
                Party party = PartyApi.API.getPlayerParty(player.getUUID());
                if (party == null) return;
                if (!party.members().get(player.getUUID()).getState().isLeader()) return;
                Guild guild = GuildApi.API.getPlayerGuild(player.getServer(), player.getUUID());
                if (guild == null) return;
                Member member = guild.members().get(player.getUUID());
                if (!member.hasPermission(MemberPermissions.TEMPORARY_GUILD_PERMISSIONS) && !member.getState().isLeader()) {
                    return;
                }
                if (!NetworkHandler.CHANNEL.canSendToPlayer(player, ClientboundReceiveShowCadmusPermissionsPacket.TYPE)) {
                    return;
                }
                NetworkHandler.CHANNEL.sendToPlayer(new ClientboundReceiveShowCadmusPermissionsPacket(), player);
            };
        }
    }
}
