package earth.terrarium.argonauts.common.network.messages;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import earth.terrarium.argonauts.client.screens.party.members.PartyMembersScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ClientboundReceiveShowCadmusPermissionsPacket() implements Packet<ClientboundReceiveShowCadmusPermissionsPacket> {

    public static final ClientboundPacketType<ClientboundReceiveShowCadmusPermissionsPacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundReceiveShowCadmusPermissionsPacket> type() {
        return TYPE;
    }

    private static class Type implements ClientboundPacketType<ClientboundReceiveShowCadmusPermissionsPacket> {

        @Override
        public Class<ClientboundReceiveShowCadmusPermissionsPacket> type() {
            return ClientboundReceiveShowCadmusPermissionsPacket.class;
        }

        @Override
        public ResourceLocation id() {
            return new ResourceLocation("argonauts", "show_cadmus_permissions");
        }

        @Override
        public void encode(ClientboundReceiveShowCadmusPermissionsPacket message, FriendlyByteBuf buffer) {}

        @Override
        public ClientboundReceiveShowCadmusPermissionsPacket decode(FriendlyByteBuf buffer) {
            return new ClientboundReceiveShowCadmusPermissionsPacket();
        }

        @Override
        public Runnable handle(ClientboundReceiveShowCadmusPermissionsPacket packet) {
            return () -> {
                if (Minecraft.getInstance().screen instanceof PartyMembersScreen screen) {
                    screen.showCadmusPermissions();
                }
            };
        }
    }
}
