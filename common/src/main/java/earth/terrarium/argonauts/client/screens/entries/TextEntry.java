package earth.terrarium.argonauts.client.screens.entries;

import com.teamresourceful.resourcefullib.client.components.selection.ListEntry;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class TextEntry extends ListEntry {

    private final Component left;
    private final Component right;

    public TextEntry(Component left, Component right) {
        this.left = left;
        this.right = right;
    }

    @Override
    protected void render(@NotNull GuiGraphics graphics, @NotNull ScissorBoxStack scissorStack, int id, int left, int top, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick, boolean selected) {
        graphics.drawString(
            Minecraft.getInstance().font,
            this.left, left + 5, top + 7, 0xFFFFFF,
            false
        );
        graphics.drawString(
            Minecraft.getInstance().font,
            this.right, left + width - Minecraft.getInstance().font.width(this.right) - 5, top + 7, 0xFFFFFF,
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
