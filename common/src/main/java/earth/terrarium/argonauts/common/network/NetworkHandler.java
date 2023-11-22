package earth.terrarium.argonauts.common.network;

import com.teamresourceful.resourcefullib.common.networking.NetworkChannel;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.common.network.messages.*;
import net.minecraft.network.protocol.PacketFlow;

public class NetworkHandler {

    public static final NetworkChannel CHANNEL = new NetworkChannel(Argonauts.MOD_ID, 1, "main", true);

    public static void init() {
        CHANNEL.registerPacket(PacketFlow.SERVERBOUND, ServerboundSetSettingPacket.ID, ServerboundSetSettingPacket.HANDLER, ServerboundSetSettingPacket.class);
        CHANNEL.registerPacket(PacketFlow.SERVERBOUND, ServerboundSetPermissionPacket.ID, ServerboundSetPermissionPacket.HANDLER, ServerboundSetPermissionPacket.class);
        CHANNEL.registerPacket(PacketFlow.SERVERBOUND, ServerboundSetRolePacket.ID, ServerboundSetRolePacket.HANDLER, ServerboundSetRolePacket.class);
        CHANNEL.registerPacket(PacketFlow.SERVERBOUND, ServerboundChatWindowPacket.ID, ServerboundChatWindowPacket.HANDLER, ServerboundChatWindowPacket.class);
        CHANNEL.registerPacket(PacketFlow.SERVERBOUND, ServerboundRequestShowCadmusPermissionsPacket.ID, ServerboundRequestShowCadmusPermissionsPacket.HANDLER, ServerboundRequestShowCadmusPermissionsPacket.class);

        CHANNEL.registerPacket(PacketFlow.CLIENTBOUND, ClientboundDeleteMessagePacket.ID, ClientboundDeleteMessagePacket.HANDLER, ClientboundDeleteMessagePacket.class);
        CHANNEL.registerPacket(PacketFlow.CLIENTBOUND, ClientboundReceiveMessagePacket.ID, ClientboundReceiveMessagePacket.HANDLER, ClientboundReceiveMessagePacket.class);
        CHANNEL.registerPacket(PacketFlow.CLIENTBOUND, ClientboundReceiveShowCadmusPermissionsPacket.ID, ClientboundReceiveShowCadmusPermissionsPacket.HANDLER, ClientboundReceiveShowCadmusPermissionsPacket.class);

        CHANNEL.registerPacket(PacketFlow.CLIENTBOUND, ClientboundOpenChatMenuPacket.ID, ClientboundOpenChatMenuPacket.HANDLER, ClientboundOpenChatMenuPacket.class);
        CHANNEL.registerPacket(PacketFlow.CLIENTBOUND, ClientboundOpenGuildMemberMenuPacket.ID, ClientboundOpenGuildMemberMenuPacket.HANDLER, ClientboundOpenGuildMemberMenuPacket.class);
        CHANNEL.registerPacket(PacketFlow.CLIENTBOUND, ClientboundOpenPartyMemberMenuPacket.ID, ClientboundOpenPartyMemberMenuPacket.HANDLER, ClientboundOpenPartyMemberMenuPacket.class);
        CHANNEL.registerPacket(PacketFlow.CLIENTBOUND, ClientboundOpenPartySettingsMenuPacket.ID, ClientboundOpenPartySettingsMenuPacket.HANDLER, ClientboundOpenPartySettingsMenuPacket.class);

        CHANNEL.registerPacket(PacketFlow.CLIENTBOUND, ClientboundSyncGuildsPacket.ID, ClientboundSyncGuildsPacket.HANDLER, ClientboundSyncGuildsPacket.class);
        CHANNEL.registerPacket(PacketFlow.CLIENTBOUND, ClientboundSyncPartiesPacket.ID, ClientboundSyncPartiesPacket.HANDLER, ClientboundSyncPartiesPacket.class);
    }
}
