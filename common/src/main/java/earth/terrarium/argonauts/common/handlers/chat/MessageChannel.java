package earth.terrarium.argonauts.common.handlers.chat;

import com.google.common.primitives.UnsignedInteger;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;

import java.util.LinkedHashMap;

public class MessageChannel {
    private UnsignedInteger nextId = UnsignedInteger.ZERO;
    private final LinkedHashMap<UnsignedInteger, ChatMessage> messages = new LinkedHashMap<>();
    private final Logger LOGGER = LogUtils.getLogger();

    public UnsignedInteger add(ChatMessage message) {
        if (messages.size() >= ChatHandler.MAX_MESSAGE_COUNT) {
            messages.remove(messages.keySet().iterator().next());
        }
        messages.put(nextId, message);
        nextId = nextId.plus(UnsignedInteger.ONE);
        LOGGER.info(String.format("<%s> %s", message.profile().getName(), message.message()));
        return nextId.minus(UnsignedInteger.ONE);
    }

    public void remove(UnsignedInteger id) {
        messages.remove(id);
    }

    public LinkedHashMap<UnsignedInteger, ChatMessage> messages() {
        return messages;
    }
}