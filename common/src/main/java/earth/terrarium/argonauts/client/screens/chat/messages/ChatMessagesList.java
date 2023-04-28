package earth.terrarium.argonauts.client.screens.chat.messages;

import com.google.common.primitives.UnsignedInteger;
import com.teamresourceful.resourcefullib.client.components.selection.ListEntry;
import com.teamresourceful.resourcefullib.client.components.selection.SelectionList;
import earth.terrarium.argonauts.common.handlers.chat.ChatMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatMessagesList extends SelectionList<ListEntry> {

    private static final Pattern URL_PATTERN = Pattern.compile("https?://(?:www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b(?:[-a-zA-Z0-9()@:%_+.~#?&/=]*)");

    public ChatMessagesList(int x, int y) {
        super(x, y, 184, 120, 10, entry -> {}, true);
    }

    public void update(LinkedHashMap<UnsignedInteger, ChatMessage> messages) {
        ListEntry last = null;
        for (var entry : messages.entrySet()) {
            UnsignedInteger id = entry.getKey();
            ChatMessage message = entry.getValue();
            last = addMessage(id, message);
        }
        if (last != null) {
            ensureVisible(last);
        }
    }

    public ListEntry addMessage(UnsignedInteger id, ChatMessage message) {
        ListEntry last = null;
        addEntry(new ChatPlayerListEntry(id, message));
        Component text = formatComponent(message.message());
        for (var sequence : Minecraft.getInstance().font.split(text, 176)) {
            last = new ChatMessageListEntry(id, message, sequence);
            addEntry(last);
        }
        return last;
    }

    private static Component formatComponent(String text) {
        text = text.replace("\n", " ");
        Matcher matcher = URL_PATTERN.matcher(text);
        MutableComponent component = Component.empty();
        int last = 0;
        while (matcher.find()) {
            String url = matcher.group();
            component.append(Component.literal(text.substring(last, matcher.start())));
            Component link = Component.literal(url).withStyle(style -> style.withUnderlined(true)
                .withColor(ChatFormatting.BLUE).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url)));
            component.append(link);
            last = matcher.end();
        }
        component.append(Component.literal(text.substring(last)));
        return component;
    }

    public void deleteMessage(UnsignedInteger id) {
        List<ListEntry> toRemove = new ArrayList<>();
        for (var child : children()) {
            if (child instanceof ChatPlayerListEntry entry) {
                if (entry.id().equals(id)) {
                    toRemove.add(entry);
                }
            } else if (child instanceof ChatMessageListEntry entry) {
                if (entry.id().equals(id)) {
                    toRemove.add(entry);
                }
            }
        }
        for (var entry : toRemove) {
            removeEntry(entry);
        }
    }
}
