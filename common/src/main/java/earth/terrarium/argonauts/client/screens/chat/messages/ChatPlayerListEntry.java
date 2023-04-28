package earth.terrarium.argonauts.client.screens.chat.messages;

import com.google.common.primitives.UnsignedInteger;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.components.selection.ListEntry;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import earth.terrarium.argonauts.common.handlers.chat.ChatMessage;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.jetbrains.annotations.NotNull;

public class ChatPlayerListEntry extends ListEntry {

    private final UnsignedInteger id;
    private final ChatMessage message;

    public ChatPlayerListEntry(UnsignedInteger id, ChatMessage message) {
        this.id = id;
        this.message = message;
    }

    @Override
    protected void render(@NotNull ScissorBoxStack scissorStack, @NotNull PoseStack stack, int id, int left, int top, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick, boolean selected) {
        if (this.id.mod(ModUtils.UNSIGNED_TWO).equals(UnsignedInteger.ZERO)) {
            Gui.fill(stack, left, top, left + width, top + height, 0x80000000);
        }
        Minecraft.getInstance().font.draw(stack, message.profile().getName() + ":", left + 2, top + 1, 0xFFFFFF);
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
