package earth.terrarium.argonauts.common.network.messages;

import com.google.common.primitives.UnsignedInteger;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.chat.CustomChatScreen;
import earth.terrarium.argonauts.common.handlers.chat.ChatMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ClientboundReceiveMessagePacket(UnsignedInteger id,
                                              ChatMessage message) implements Packet<ClientboundReceiveMessagePacket> {

    public static final ClientboundPacketType<ClientboundReceiveMessagePacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundReceiveMessagePacket> type() {
        return TYPE;
    }

    private static class Type implements ClientboundPacketType<ClientboundReceiveMessagePacket> {

        @Override
        public Class<ClientboundReceiveMessagePacket> type() {
            return ClientboundReceiveMessagePacket.class;
        }

        @Override
        public ResourceLocation id() {
            return new ResourceLocation(Argonauts.MOD_ID, "receive_message");
        }

        @Override
        public void encode(ClientboundReceiveMessagePacket message, FriendlyByteBuf buffer) {
            buffer.writeVarInt(message.id.intValue());
            message.message.encode(buffer);
        }

        @Override
        public ClientboundReceiveMessagePacket decode(FriendlyByteBuf buffer) {
            return new ClientboundReceiveMessagePacket(
                UnsignedInteger.valueOf(buffer.readVarInt()),
                ChatMessage.decode(buffer)
            );
        }

        @Override
        public Runnable handle(ClientboundReceiveMessagePacket packet) {
            return () -> {
                if (Minecraft.getInstance().screen instanceof CustomChatScreen screen) {
                    screen.addMessage(packet.id, packet.message);
                }
            };
        }
    }
}
