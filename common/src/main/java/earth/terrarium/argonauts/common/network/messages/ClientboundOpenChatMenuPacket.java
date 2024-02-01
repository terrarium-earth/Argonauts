package earth.terrarium.argonauts.common.network.messages;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.chat.CustomChatScreen;
import earth.terrarium.argonauts.common.menus.ChatContent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record ClientboundOpenChatMenuPacket(ChatContent menuContent,
                                            Component displayName) implements Packet<ClientboundOpenChatMenuPacket> {

    public static final ClientboundPacketType<ClientboundOpenChatMenuPacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundOpenChatMenuPacket> type() {
        return TYPE;
    }

    private static class Type implements ClientboundPacketType<ClientboundOpenChatMenuPacket> {

        @Override
        public Class<ClientboundOpenChatMenuPacket> type() {
            return ClientboundOpenChatMenuPacket.class;
        }

        @Override
        public ResourceLocation id() {
            return new ResourceLocation(Argonauts.MOD_ID, "open_chat_menu");
        }

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
        public Runnable handle(ClientboundOpenChatMenuPacket packet) {
            return () -> CustomChatScreen.open(packet.menuContent, packet.displayName);
        }
    }
}
