package earth.terrarium.argonauts.client.screens.party.members.entries;

import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.components.selection.ListEntry;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import net.minecraft.client.Minecraft;
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
    protected void render(@NotNull ScissorBoxStack scissorStack, @NotNull PoseStack stack, int id, int left, int top, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick, boolean selected) {
        Minecraft.getInstance().font.draw(stack, this.left, left + 5, top + 7, 0xFFFFFF);
        Minecraft.getInstance().font.draw(stack, this.right, left + width - Minecraft.getInstance().font.width(this.right) - 5, top + 7, 0xFFFFFF);
    }

    @Override
    public void setFocused(boolean bl) {}

    @Override
    public boolean isFocused() {
        return false;
    }
}
