package earth.terrarium.argonauts.common.network.messages;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.chat.CustomChatScreen;
import earth.terrarium.argonauts.common.menus.ChatContent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record ClientboundOpenChatMenuPacket(ChatContent menuContent,
                                            Component displayName) implements Packet<ClientboundOpenChatMenuPacket> {

    public static final ResourceLocation ID = new ResourceLocation(Argonauts.MOD_ID, "open_chat_menu");
    public static final PacketHandler<ClientboundOpenChatMenuPacket> HANDLER = new Handler();

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<ClientboundOpenChatMenuPacket> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<ClientboundOpenChatMenuPacket> {

        @Override
        public void encode(ClientboundOpenChatMenuPacket message, FriendlyByteBuf buffer) {
            message.menuContent.serializer().to(buffer, message.menuContent);
            buffer.writeComponent(message.displayName);
        }

        @Override
        public ClientboundOpenChatMenuPacket decode(FriendlyByteBuf buffer) {
            return new ClientboundOpenChatMenuPacket(
                ChatContent.SERIALIZER.from(buffer),
                buffer.readComponent()
            );
        }

        @Override
        public PacketContext handle(ClientboundOpenChatMenuPacket message) {
            return (player, level) -> CustomChatScreen.open(message.menuContent, message.displayName);
        }
    }
}
