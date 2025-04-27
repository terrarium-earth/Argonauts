package earth.terrarium.argonauts.client.screens.chat.messages;

import com.teamresourceful.resourcefullib.client.components.selection.ListEntry;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import earth.terrarium.argonauts.common.chat.ChatMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

public class ChatPlayerListEntry extends ListEntry {

    private final int id;
    private final ChatMessage message;

    public ChatPlayerListEntry(int id, ChatMessage message) {
        this.id = id;
        this.message = message;
    }

    @Override
    protected void render(@NotNull GuiGraphics graphics, @NotNull ScissorBoxStack scissorStack, int id, int left, int top, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick, boolean selected) {
        if (this.id % 2 == 0) {
            graphics.fill(left, top, left + width, top + height, 0x80000000);
        }
        graphics.drawString(
            Minecraft.getInstance().font,
            message.profile().getName() + ":", left + 2, top + 1, 0xFFFFFF,
            false
        );
    }

    @Override
    public void setFocused(boolean focused) {}

    @Override
    public boolean isFocused() {
        return false;
    }
}
