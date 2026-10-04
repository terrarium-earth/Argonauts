package earth.terrarium.argonauts.common.network;


import com.teamresourceful.resourcefullib.common.network.Network;
import com.teamresourceful.resourcefullib.common.network.Packet;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.common.network.packets.*;
import net.minecraft.server.MinecraftServer;

public class NetworkHandler {

    public static final Network CHANNEL = new Network(Argonauts.id("main"), 1, true);

    public static void init() {
        CHANNEL.register(ClientboundAddGuildPacket.TYPE);
        CHANNEL.register(ClientboundRemoveGuildPacket.TYPE);
        CHANNEL.register(ClientboundSyncGuildsPacket.TYPE);
        CHANNEL.register(ClientboundModifyGuildMemberPacket.TYPE);
        CHANNEL.register(ClientboundModifyGuildPermissionPacket.TYPE);
        CHANNEL.register(ClientboundLeaveGuildPacket.TYPE);
        CHANNEL.register(ClientboundModifyGuildSettingPacket.TYPE);

        CHANNEL.register(ClientboundAddPartyPacket.TYPE);
        CHANNEL.register(ClientboundRemovePartyPacket.TYPE);
        CHANNEL.register(ClientboundSyncPartiesPacket.TYPE);
        CHANNEL.register(ClientboundModifyPartyMemberPacket.TYPE);
        CHANNEL.register(ClientboundModifyPartyPermissionPacket.TYPE);
        CHANNEL.register(ClientboundLeavePartyPacket.TYPE);
        CHANNEL.register(ClientboundModifyPartySettingPacket.TYPE);

        CHANNEL.register(ClientboundSendMessagePacket.TYPE);
    }

    /**
     * Sends to all clients that have Odyssey Guilds installed
     */
    public static <T extends Packet<T>> void sendToAllClientPlayers(T packet, MinecraftServer server) {
        server.getPlayerList().getPlayers().forEach(player -> {
            if (CHANNEL.canSendToPlayer(player, packet.type())) {
                CHANNEL.sendToPlayer(packet, player);
            }
        });
    }
}
