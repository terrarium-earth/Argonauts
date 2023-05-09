package earth.terrarium.argonauts.common.network;

import com.teamresourceful.resourcefullib.common.networking.NetworkChannel;
import com.teamresourceful.resourcefullib.common.networking.base.NetworkDirection;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.common.network.messages.*;

public class NetworkHandler {

    public static final NetworkChannel CHANNEL = new NetworkChannel(Argonauts.MOD_ID, 1, "main");

    public static void init() {
        CHANNEL.registerPacket(NetworkDirection.CLIENT_TO_SERVER, ServerboundSetSettingPacket.ID, ServerboundSetSettingPacket.HANDLER, ServerboundSetSettingPacket.class);
        CHANNEL.registerPacket(NetworkDirection.CLIENT_TO_SERVER, ServerboundSetPermissionPacket.ID, ServerboundSetPermissionPacket.HANDLER, ServerboundSetPermissionPacket.class);
        CHANNEL.registerPacket(NetworkDirection.CLIENT_TO_SERVER, ServerboundSetRolePacket.ID, ServerboundSetRolePacket.HANDLER, ServerboundSetRolePacket.class);
        CHANNEL.registerPacket(NetworkDirection.CLIENT_TO_SERVER, ServerboundChatWindowPacket.ID, ServerboundChatWindowPacket.HANDLER, ServerboundChatWindowPacket.class);
        CHANNEL.registerPacket(NetworkDirection.CLIENT_TO_SERVER, ServerboundRequestShowCadmusPermissionsPacket.ID, ServerboundRequestShowCadmusPermissionsPacket.HANDLER, ServerboundRequestShowCadmusPermissionsPacket.class);

        CHANNEL.registerPacket(NetworkDirection.SERVER_TO_CLIENT, ClientboundDeleteMessagePacket.ID, ClientboundDeleteMessagePacket.HANDLER, ClientboundDeleteMessagePacket.class);
        CHANNEL.registerPacket(NetworkDirection.SERVER_TO_CLIENT, ClientboundReceiveMessagePacket.ID, ClientboundReceiveMessagePacket.HANDLER, ClientboundReceiveMessagePacket.class);
        CHANNEL.registerPacket(NetworkDirection.SERVER_TO_CLIENT, ClientboundReceiveShowCadmusPermissionsPacket.ID, ClientboundReceiveShowCadmusPermissionsPacket.HANDLER, ClientboundReceiveShowCadmusPermissionsPacket.class);
    }
}
