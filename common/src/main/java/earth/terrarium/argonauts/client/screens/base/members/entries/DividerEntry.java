package earth.terrarium.argonauts.client.screens.base.members.entries;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.components.selection.ListEntry;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
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
    protected void render(@NotNull ScissorBoxStack scissorStack, @NotNull PoseStack stack, int id, int left, int top, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick, boolean selected) {
        int center = (int) (height / 2f);
        if (text != null) {
            Gui.fill(stack, left + 5, top + center - 1, left + 9, top + center, 0xFFFFFFFF);
            int lineHeight = Minecraft.getInstance().font.lineHeight;
            int end = Minecraft.getInstance().font.draw(stack, text, left + 10, top + center - (lineHeight / 2f), 0xFFFFFFFF);
            Gui.fill(stack, end + 1, top + center - 1, left + width - 5, top + center, 0xFFFFFFFF);
        } else {
            Gui.fill(stack, left + 5, top + center - 1, left + width - 5, top + center, 0xFFFFFFFF);
        }
    }

    @Override
    public void setFocused(boolean bl) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }
}
