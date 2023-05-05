package earth.terrarium.argonauts.common.handlers.chat;

import earth.terrarium.argonauts.common.handlers.base.members.Group;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatHandler {

    public static final int MAX_MESSAGE_LENGTH = 512;
    public static final int MAX_MESSAGE_COUNT = 200;

    private static final Map<ChatMessageType, Map<UUID, MessageChannel>> CHANNELS = new HashMap<>();

    private static Map<UUID, MessageChannel> getChannels(ChatMessageType type) {
        return CHANNELS.computeIfAbsent(type, t -> new HashMap<>());
    }

    public static MessageChannel getChannel(Group<?> group, ChatMessageType type) {
        return getChannels(type).computeIfAbsent(group.id(), i -> new MessageChannel());
    }

    public static void remove(Group<?> group, ChatMessageType type) {
        getChannels(type).remove(group.id());
    }
}
