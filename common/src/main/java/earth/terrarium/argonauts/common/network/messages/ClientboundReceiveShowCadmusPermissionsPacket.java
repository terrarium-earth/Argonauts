package earth.terrarium.argonauts.common.network.messages;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.party.members.PartyMembersScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ClientboundReceiveShowCadmusPermissionsPacket() implements Packet<ClientboundReceiveShowCadmusPermissionsPacket> {

    public static final ResourceLocation ID = new ResourceLocation(Argonauts.MOD_ID, "receive_show_cadmus_permissions");
    public static final Handler HANDLER = new Handler();

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<ClientboundReceiveShowCadmusPermissionsPacket> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<ClientboundReceiveShowCadmusPermissionsPacket> {

        @Override
        public void encode(ClientboundReceiveShowCadmusPermissionsPacket message, FriendlyByteBuf buffer) {
        }

        @Override
        public ClientboundReceiveShowCadmusPermissionsPacket decode(FriendlyByteBuf buffer) {
            return new ClientboundReceiveShowCadmusPermissionsPacket();
        }

        @Override
        public PacketContext handle(ClientboundReceiveShowCadmusPermissionsPacket message) {
            return (player, level) -> {
                if (Minecraft.getInstance().screen instanceof PartyMembersScreen screen) {
                    screen.showCadmusPermissions();
                }
            };
        }
    }
}
