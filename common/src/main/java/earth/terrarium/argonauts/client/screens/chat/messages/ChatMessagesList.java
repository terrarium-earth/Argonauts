package earth.terrarium.argonauts.client.screens.chat.messages;

import com.teamresourceful.resourcefullib.client.components.selection.ListEntry;
import com.teamresourceful.resourcefullib.client.components.selection.SelectionList;
import earth.terrarium.argonauts.common.chat.ChatMessage;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatMessagesList extends SelectionList<ListEntry> {

    private static final Pattern URL_PATTERN = Pattern.compile("https?://(?:www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b[-a-zA-Z0-9()@:%_+.~#?&/=]*");

    private int messageCount;

    public ChatMessagesList(int x, int y, Set<ChatMessage> messages) {
        super(x, y, 184, 120, 10, entry -> {}, true);
        update(messages);
    }

    public void update(Set<ChatMessage> messages) {
        ListEntry last = null;
        for (var message : messages) {
            last = addMessage(message);
        }
        if (last != null) {
            ensureVisible(last);
        }
    }

    public ListEntry addMessage(ChatMessage message) {
        ListEntry last = null;
        addEntry(new ChatPlayerListEntry(this.messageCount, message));
        Component text = formatComponent(message.message());
        for (var sequence : Minecraft.getInstance().font.split(text, 176)) {
            last = new ChatMessageListEntry(this.messageCount, sequence);
            addEntry(last);
        }
        this.messageCount++;
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
}
