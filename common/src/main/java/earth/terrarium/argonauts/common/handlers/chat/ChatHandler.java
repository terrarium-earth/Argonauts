package earth.terrarium.argonauts.common.handlers.chat;

import earth.terrarium.argonauts.common.handlers.party.Party;

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

    public static MessageChannel getPartyChannel(Party party) {
        return getChannels(ChatMessageType.PARTY).computeIfAbsent(party.id(), i -> new MessageChannel());
    }

    public static MessageChannel getGuildChannel(UUID guild) {
        return getChannels(ChatMessageType.GUILD).computeIfAbsent(guild, i -> new MessageChannel());
    }

    public static void removeParty(Party party) {
        getChannels(ChatMessageType.PARTY).remove(party.id());
    }

    //TODO Change to guild id
    public static void removeGuild(UUID guild) {
        getChannels(ChatMessageType.GUILD).remove(guild);
    }

}
