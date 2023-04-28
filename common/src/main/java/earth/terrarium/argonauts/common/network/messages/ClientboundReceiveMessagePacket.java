package earth.terrarium.argonauts.common.network.messages;

import com.google.common.primitives.UnsignedInteger;
import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.chat.CustomChatScreen;
import earth.terrarium.argonauts.common.handlers.chat.ChatMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ClientboundReceiveMessagePacket(UnsignedInteger id, ChatMessage message) implements Packet<ClientboundReceiveMessagePacket> {

    public static final ResourceLocation ID = new ResourceLocation(Argonauts.MOD_ID, "receive_message");
    public static final PacketHandler<ClientboundReceiveMessagePacket> HANDLER = new Handler();

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<ClientboundReceiveMessagePacket> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<ClientboundReceiveMessagePacket> {

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
        public PacketContext handle(ClientboundReceiveMessagePacket message) {
            return (player, level) -> {
                if (Minecraft.getInstance().screen instanceof CustomChatScreen screen) {
                    screen.addMessage(message.id, message.message);
                }
            };
        }
    }
}
