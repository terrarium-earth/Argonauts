
package earth.terrarium.argonauts.common.handlers.chat;

import com.mojang.authlib.GameProfile;
import net.minecraft.network.FriendlyByteBuf;

import java.time.Instant;

public record ChatMessage(GameProfile profile, String message, Instant timestamp) {

    public static final int MAX_MESSAGE_LENGTH = 512;

    public ChatMessage {
        if (message.length() > MAX_MESSAGE_LENGTH) {
            throw new IllegalArgumentException("Message length exceeds maximum length of " + MAX_MESSAGE_LENGTH);
        }
    }

    public static ChatMessage decode(FriendlyByteBuf buf) {
        return new ChatMessage(buf.readGameProfile(), buf.readUtf(ChatMessage.MAX_MESSAGE_LENGTH), buf.readInstant());
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeGameProfile(profile);
        buf.writeUtf(message, ChatMessage.MAX_MESSAGE_LENGTH);
        buf.writeInstant(timestamp);
    }

}