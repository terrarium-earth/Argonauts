package earth.terrarium.argonauts.common.network.messages;

import com.google.common.primitives.UnsignedInteger;
import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.chat.CustomChatScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ClientboundDeleteMessagePacket(UnsignedInteger id) implements Packet<ClientboundDeleteMessagePacket> {

    public static final ResourceLocation ID = new ResourceLocation(Argonauts.MOD_ID, "delete_message");
    public static final PacketHandler<ClientboundDeleteMessagePacket> HANDLER = new Handler();

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<ClientboundDeleteMessagePacket> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<ClientboundDeleteMessagePacket> {

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
        public PacketContext handle(ClientboundDeleteMessagePacket message) {
            return (player, level) -> {
                if (Minecraft.getInstance().screen instanceof CustomChatScreen screen) {
                    screen.deleteMessage(message.id);
                }
            };
        }
    }
}
