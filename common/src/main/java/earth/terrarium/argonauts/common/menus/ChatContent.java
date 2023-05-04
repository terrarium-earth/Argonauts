package earth.terrarium.argonauts.common.menus;

import com.google.common.primitives.UnsignedInteger;
import com.teamresourceful.resourcefullib.common.menu.MenuContent;
import com.teamresourceful.resourcefullib.common.menu.MenuContentSerializer;
import earth.terrarium.argonauts.common.handlers.chat.ChatMessage;
import earth.terrarium.argonauts.common.handlers.chat.ChatMessageType;
import net.minecraft.network.FriendlyByteBuf;

import java.util.LinkedHashMap;
import java.util.List;

public record ChatContent(ChatMessageType type, int maxUsers, List<String> usernames,
                          LinkedHashMap<UnsignedInteger, ChatMessage> messages) implements MenuContent<ChatContent> {

    public static final MenuContentSerializer<ChatContent> SERIALIZER = new Serializer();

    @Override
    public MenuContentSerializer<ChatContent> serializer() {
        return SERIALIZER;
    }

    private static class Serializer implements MenuContentSerializer<ChatContent> {

        @Override
        public ChatContent from(FriendlyByteBuf buffer) {
            return new ChatContent(
                buffer.readEnum(ChatMessageType.class),
                buffer.readVarInt(),
                buffer.readList(buf -> buf.readUtf(16)),
                buffer.readMap(LinkedHashMap::new, buf -> UnsignedInteger.fromIntBits(buf.readVarInt()), ChatMessage::decode)
            );
        }

        @Override
        public void to(FriendlyByteBuf buffer, ChatContent content) {
            buffer.writeEnum(content.type());
            buffer.writeVarInt(content.maxUsers());
            buffer.writeCollection(content.usernames(), (buf, user) -> buf.writeUtf(user, 16));
            buffer.writeMap(
                content.messages(),
                (buf, key) -> buf.writeVarInt(key.intValue()),
                (buf, value) -> value.encode(buf)
            );
        }
    }
}
