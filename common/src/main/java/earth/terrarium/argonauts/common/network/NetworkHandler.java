package earth.terrarium.argonauts.common.network;

import com.teamresourceful.resourcefullib.common.network.Network;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.common.network.messages.*;
import net.minecraft.resources.ResourceLocation;

public class NetworkHandler {

    public static final Network CHANNEL = new Network(new ResourceLocation(Argonauts.MOD_ID, "main"), 1, true);

    public static void init() {
        CHANNEL.register(ServerboundSetSettingPacket.TYPE);
        CHANNEL.register(ServerboundSetPermissionPacket.TYPE);
        CHANNEL.register(ServerboundSetRolePacket.TYPE);
        CHANNEL.register(ServerboundChatWindowPacket.TYPE);
        CHANNEL.register(ServerboundRequestShowCadmusPermissionsPacket.TYPE);

        CHANNEL.register(ClientboundDeleteMessagePacket.TYPE);
        CHANNEL.register(ClientboundReceiveMessagePacket.TYPE);
        CHANNEL.register(ClientboundReceiveShowCadmusPermissionsPacket.TYPE);
        CHANNEL.register(ClientboundOpenChatMenuPacket.TYPE);
        CHANNEL.register(ClientboundOpenGuildMemberMenuPacket.TYPE);
        CHANNEL.register(ClientboundOpenPartyMemberMenuPacket.TYPE);
        CHANNEL.register(ClientboundOpenPartySettingsMenuPacket.TYPE);
        CHANNEL.register(ClientboundSyncGuildsPacket.TYPE);
        CHANNEL.register(ClientboundSyncPartiesPacket.TYPE);
    }
}
