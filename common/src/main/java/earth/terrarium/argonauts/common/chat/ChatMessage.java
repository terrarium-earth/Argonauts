package earth.terrarium.argonauts.common.chat;

import com.mojang.authlib.GameProfile;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import net.minecraft.network.FriendlyByteBuf;

import java.time.Instant;

public record ChatMessage(
    GameProfile profile,
    String message,
    Instant timestamp
) {

    public static final int MAX_MESSAGE_LENGTH = 245;
    public static final int ACTUAL_MAX_MESSAGE_LENGTH = 256; // Max size for a chat packet

    public ChatMessage {
        if (message.length() > ACTUAL_MAX_MESSAGE_LENGTH) {
            throw new IllegalArgumentException("Message length exceeds maximum length of " + MAX_MESSAGE_LENGTH);
        }
    }

    private static final ByteCodec<GameProfile> GAME_PROFILE_CODEC = ObjectByteCodec.create(
        ByteCodec.UUID.fieldOf(GameProfile::getId),
        ByteCodec.STRING.fieldOf(GameProfile::getName),
        GameProfile::new
    );

    public static final ByteCodec<ChatMessage> BYTE_CODEC = ObjectByteCodec.create(
        GAME_PROFILE_CODEC.fieldOf(ChatMessage::profile),
        ByteCodec.STRING.fieldOf(ChatMessage::message),
        ByteCodec.passthrough((buf, instant) -> ((FriendlyByteBuf) buf).writeInstant(instant),
            buf -> ((FriendlyByteBuf) buf).readInstant()).fieldOf(ChatMessage::timestamp),
        ChatMessage::new
    );
}