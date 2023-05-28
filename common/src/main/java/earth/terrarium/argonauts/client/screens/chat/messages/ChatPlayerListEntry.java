package earth.terrarium.argonauts.client.screens.chat.messages;

import com.google.common.primitives.UnsignedInteger;
import com.teamresourceful.resourcefullib.client.components.selection.ListEntry;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import earth.terrarium.argonauts.common.handlers.chat.ChatMessage;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import org.jetbrains.annotations.NotNull;

public class ChatPlayerListEntry extends ListEntry {

    private final UnsignedInteger id;
    private final ChatMessage message;

    public ChatPlayerListEntry(UnsignedInteger id, ChatMessage message) {
        this.id = id;
        this.message = message;
    }

    @Override
    protected void render(@NotNull GuiGraphics graphics, @NotNull ScissorBoxStack scissorStack, int id, int left, int top, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick, boolean selected) {
        if (this.id.mod(ModUtils.UNSIGNED_TWO).equals(UnsignedInteger.ZERO)) {
            graphics.fill(left, top, left + width, top + height, 0x80000000);
        }
        graphics.drawString(
            Minecraft.getInstance().font,
            message.profile().getName() + ":", left + 2, top + 1, 0xFFFFFF,
            false
        );
    }

    public UnsignedInteger id() {
        return this.id;
    }

    @Override
    public void setFocused(boolean bl) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }
}
