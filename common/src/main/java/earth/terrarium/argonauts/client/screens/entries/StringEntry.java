package earth.terrarium.argonauts.client.screens.entries;

import com.mojang.blaze3d.platform.InputConstants;
import com.teamresourceful.resourcefullib.client.components.selection.ListEntry;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import com.teamresourceful.resourcefullib.client.utils.CursorUtils;
import com.teamresourceful.resourcefullib.client.utils.RenderUtils;
import com.teamresourceful.resourcefullib.client.utils.ScreenUtils;
import earth.terrarium.argonauts.Argonauts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class StringEntry extends ListEntry {

    private static final ResourceLocation CONTAINER_BACKGROUND = Argonauts.id("textures/gui/members.png");

    private final Component title;
    private final Component description;
    private String text;
    private final boolean canEdit;
    private final Consumer<String> onSave;
    private String savedText;

    public StringEntry(Component title, Component description, String text, boolean canEdit, Consumer<String> onSave) {
        this.title = title;
        this.description = description;
        this.text = text;
        this.canEdit = canEdit;
        this.onSave = onSave;
        this.savedText = text;
    }

    @Override
    protected void render(@NotNull GuiGraphics graphics, @NotNull ScissorBoxStack scissorStack, int id, int left, int top, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick, boolean selected) {
        Font font = Minecraft.getInstance().font;
        graphics.drawString(
            font,
            title,
            left + 5,
            top + (int) (height / 2f) - (int) (font.lineHeight / 2f),
            canEdit ? 0xFFFFFF : 0x888888,
            false
        );

        int borderColor = selected && canEdit ? 0xFFFFFFFF : 0xFF808080;

        graphics.fill(left + width - 5 - 102, top + 2, left + width - 25, top + height - 2, borderColor);
        graphics.fill(left + width - 5 - 101, top + 3, left + width - 26, top + height - 3, 0xFF000000);

        try (var ignored = RenderUtils.createScissorBoxStack(scissorStack, Minecraft.getInstance(), graphics.pose(), left + width - 5 - 101, top + 3, 80, height - 4)) {
            graphics.drawString(
                font,
                text, left + width - 5 - 99, top + 6, canEdit ? 0xFFFFFF : 0x505050,
                false
            );
        }

        boolean buttonHovered = hovered && mouseX >= left + width - 22 && mouseX < left + width - 6 && mouseY >= top + 2 && mouseY <= top + height - 2;

        int offset = !canEdit || text.equals(savedText) ? 32 : buttonHovered ? 16 : 0;
        graphics.blit(CONTAINER_BACKGROUND, left + width - 22, top + 2, 346, offset, 16, 16, 512, 512);

        if (hovered && mouseX >= left + width - 5 - 101 && mouseX < left + width - 25 && mouseY >= top + 3 && mouseY <= top + height - 3) {
            CursorUtils.setCursor(true, canEdit ? CursorScreen.Cursor.TEXT : CursorScreen.Cursor.DISABLED);
        } else if (buttonHovered) {
            CursorUtils.setCursor(true, canEdit && !text.equals(savedText) ? CursorScreen.Cursor.POINTER : CursorScreen.Cursor.DISABLED);
        }

        if (hovered && mouseX >= left + 5 && mouseX < left + 5 + font.width(title) && mouseY >= top + (int) (height / 2f) - (int) (font.lineHeight / 2f) && mouseY < top + (int) (height / 2f) + (int) (font.lineHeight / 2f)) {
            ScreenUtils.setTooltip(description);
        }
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        if (canEdit) {
            text += codePoint;
            return true;
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (canEdit) {
            if (keyCode == 259 && !text.isEmpty()) {
                text = text.substring(0, text.length() - 1);
                return true;
            }
            if (Screen.isPaste(keyCode)) {
                String text = Minecraft.getInstance().keyboardHandler.getClipboard();
                this.text += text;
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (button == InputConstants.MOUSE_BUTTON_LEFT && canEdit && !savedText.equals(text)) {
            if (x >= 184 - 22 && x < 184 - 6 && y >= 2 && y <= 20 - 2) {
                savedText = text;
                onSave.accept(text);
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
