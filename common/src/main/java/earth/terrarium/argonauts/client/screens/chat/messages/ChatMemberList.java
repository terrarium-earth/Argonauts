package earth.terrarium.argonauts.client.screens.chat.messages;

import com.teamresourceful.resourcefullib.client.components.selection.ListEntry;
import com.teamresourceful.resourcefullib.client.components.selection.SelectionList;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import com.teamresourceful.resourcefullib.client.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ChatMemberList extends SelectionList<ListEntry> {

    public ChatMemberList(int x, int y) {
        super(x, y, 70, 140, 20, entry -> {});
    }

    public void update(List<String> usernames) {
        for (var username : usernames) {
            addEntry(new Entry(username));
        }
    }

    private static class Entry extends ListEntry {

        private final String username;

        public Entry(String username) {
            this.username = username;
        }

        @Override
        protected void render(@NotNull GuiGraphics graphics, @NotNull ScissorBoxStack scissorStack, int id, int left, int top, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick, boolean selected) {
            if (id % 2 == 0) {
                graphics.fill(left, top, left + width, top + height, 0x80000000);
            }
            try (var ignored = RenderUtils.createScissorBoxStack(scissorStack, Minecraft.getInstance(), graphics.pose(), left + 2, top + 2, width - 4, height - 4)) {
                graphics.drawString(
                    Minecraft.getInstance().font,
                    this.username, left + 2, top + 6, 0xFFFFFF
                );
            }
        }

        @Override
        public void setFocused(boolean focused) {

        }

        @Override
        public boolean isFocused() {
            return false;
        }
    }
}
