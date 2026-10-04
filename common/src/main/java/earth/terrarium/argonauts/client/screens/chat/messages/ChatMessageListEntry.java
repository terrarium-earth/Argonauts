package earth.terrarium.argonauts.client.screens.chat.messages;

import com.mojang.blaze3d.platform.InputConstants;
import com.teamresourceful.resourcefullib.client.components.selection.ListEntry;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import com.teamresourceful.resourcefullib.client.utils.ScreenUtils;
import earth.terrarium.argonauts.client.screens.chat.ChatScreen;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

public class ChatMessageListEntry extends ListEntry {

    private final int id;
    private final FormattedCharSequence component;

    public ChatMessageListEntry(int id, FormattedCharSequence component) {
        this.id = id;
        this.component = component;
    }

    @Override
    protected void render(@NotNull GuiGraphics graphics, @NotNull ScissorBoxStack scissorStack, int id, int left, int top, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick, boolean selected) {
        if (this.id % 2 == 0) {
            graphics.fill(left, top, left + width, top + height, 0x80000000);
        }
        graphics.drawString(
            Minecraft.getInstance().font,
            this.component, left + 8, top + 1, 0xFFFFFF,
            false
        );

        Style style = Minecraft.getInstance().font.getSplitter().componentStyleAtWidth(this.component, Mth.floor(mouseX - left));
        if (style != null && hovered) {
            if (style.getHoverEvent() != null) {
                Component component = style.getHoverEvent().getValue(HoverEvent.Action.SHOW_TEXT);
                if (component != null) {
                    ScreenUtils.setTooltip(component);
                }
            } else if (style.getClickEvent() != null) {
                ClickEvent.Action clickAction = style.getClickEvent().getAction();
                if (clickAction == ClickEvent.Action.OPEN_URL && Minecraft.getInstance().screen instanceof ChatScreen screen) {
                    screen.setEmbedUrl(style.getClickEvent().getValue());
                }
            }
        }
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (button == InputConstants.MOUSE_BUTTON_LEFT) {
            var style = Minecraft.getInstance().font.getSplitter().componentStyleAtWidth(this.component, Mth.floor(x));
            if (style != null && style.getClickEvent() != null) {
                if (style.getClickEvent().getAction() == ClickEvent.Action.OPEN_URL) {
                    Util.getPlatform().openUri(style.getClickEvent().getValue());
                } else {
                    System.out.println("Unhandled click event: " + style.getClickEvent().getAction());
                }
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
