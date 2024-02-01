package earth.terrarium.argonauts.common.network.messages;

import com.google.common.primitives.UnsignedInteger;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.chat.CustomChatScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ClientboundDeleteMessagePacket(UnsignedInteger id) implements Packet<ClientboundDeleteMessagePacket> {

    public static final ClientboundPacketType<ClientboundDeleteMessagePacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundDeleteMessagePacket> type() {
        return TYPE;
    }

    private static class Type implements ClientboundPacketType<ClientboundDeleteMessagePacket> {

        @Override
        public Class<ClientboundDeleteMessagePacket> type() {
            return ClientboundDeleteMessagePacket.class;
        }

        @Override
        public ResourceLocation id() {
            return new ResourceLocation(Argonauts.MOD_ID, "delete_message");
        }

        @Override
        public void encode(ClientboundDeleteMessagePacket message, FriendlyByteBuf buffer) {
            buffer.writeVarInt(message.id.intValue());
        }

        @Override
        public ClientboundDeleteMessagePacket decode(FriendlyByteBuf buffer) {
            return new ClientboundDeleteMessagePacket(
                UnsignedInteger.valueOf(buffer.readVarInt())
            );
        }

        @Override
        public Runnable handle(ClientboundDeleteMessagePacket packet) {
            return () -> {
                if (Minecraft.getInstance().screen instanceof CustomChatScreen screen) {
                    screen.deleteMessage(packet.id);
                }
            };
        }
    }
}
