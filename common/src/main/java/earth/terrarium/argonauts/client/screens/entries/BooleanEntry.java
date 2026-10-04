package earth.terrarium.argonauts.client.screens.entries;

import com.mojang.blaze3d.platform.InputConstants;
import com.teamresourceful.resourcefullib.client.components.selection.ListEntry;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import com.teamresourceful.resourcefullib.client.utils.CursorUtils;
import com.teamresourceful.resourcefullib.client.utils.ScreenUtils;
import earth.terrarium.argonauts.Argonauts;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BooleanEntry extends ListEntry {

    private static final ResourceLocation CONTAINER_BACKGROUND = Argonauts.id("textures/gui/members.png");

    private final Component title;
    private final Component description;
    private boolean value;
    private final boolean canEdit;
    private final BooleanConsumer onClick;

    public BooleanEntry(Component title, Component description, boolean value, boolean canEdit, BooleanConsumer onClick) {
        this.title = title;
        this.description = description;
        this.value = value;
        this.canEdit = canEdit;
        this.onClick = onClick;
    }

    @Override
    protected void render(@NotNull GuiGraphics graphics, @NotNull ScissorBoxStack scissorStack, int id, int left, int top, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick, boolean selected) {
        Font font = Minecraft.getInstance().font;
        graphics.drawString(
            font,
            title,
            left + 5,
            top + 7,
            canEdit ? 0xFFFFFF : 0x888888,
            false
        );

        if (canEdit) {
            graphics.blit(CONTAINER_BACKGROUND, left + width - 28, top + 5, 276, 122, 23, 12, 512, 512);
            if (value) {
                graphics.blit(CONTAINER_BACKGROUND, left + width - 17, top + 5, 288, 134, 12, 12, 512, 512);
            } else {
                graphics.blit(CONTAINER_BACKGROUND, left + width - 28, top + 5, 276, 134, 12, 12, 512, 512);
            }
        } else {
            graphics.blit(CONTAINER_BACKGROUND, left + width - 28, top + 5, 299, 122, 23, 12, 512, 512);
        }

        if (hovered && mouseX >= left + width - 28 && mouseX < left + width - 5) {
            CursorUtils.setCursor(true, canEdit ? CursorScreen.Cursor.POINTER : CursorScreen.Cursor.DISABLED);
        }

        if (hovered && mouseX >= left + 5 && mouseX < left + 5 + font.width(title) && mouseY >= top + (int) (height / 2f) - (int) (font.lineHeight / 2f) && mouseY < top + (int) (height / 2f) + (int) (font.lineHeight / 2f)) {
            ScreenUtils.setTooltip(description);
        }
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (button == InputConstants.MOUSE_BUTTON_LEFT && y >= 5 && y <= 17 && canEdit) {
            if (x > 156 && x < 179) {
                value = !value;
                onClick.accept(value);
                return true;
            }
        }
        return super.mouseClicked(x, y, button);
    }

    @Override
    public void setFocused(boolean focused) {}

    @Override
    public boolean isFocused() {
        return false;
    }
}
