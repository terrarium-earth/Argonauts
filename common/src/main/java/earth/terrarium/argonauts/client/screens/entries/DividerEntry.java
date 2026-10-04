package earth.terrarium.argonauts.client.screens.entries;

import com.teamresourceful.resourcefullib.client.components.selection.ListEntry;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DividerEntry extends ListEntry {

    @Nullable
    private final Component text;

    public DividerEntry(@Nullable Component text) {
        this.text = text;
    }

    @Override
    protected void render(@NotNull GuiGraphics graphics, @NotNull ScissorBoxStack scissorStack, int id, int left, int top, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick, boolean selected) {
        int center = (int) (height / 2f);
        if (text != null) {
            graphics.fill(left + 5, top + center - 1, left + 9, top + center, 0xFFFFFFFF);
            int lineHeight = Minecraft.getInstance().font.lineHeight;
            int end = graphics.drawString(
                Minecraft.getInstance().font,
                text, left + 10, top + center - (int) (lineHeight / 2f), 0xFFFFFFFF,
                false
            );
            graphics.fill(end + 1, top + center - 1, left + width - 5, top + center, 0xFFFFFFFF);
        } else {
            graphics.fill(left + 5, top + center - 1, left + width - 5, top + center, 0xFFFFFFFF);
        }
    }

    @Override
    public void setFocused(boolean focused) {}

    @Override
    public boolean isFocused() {
        return false;
    }
}
