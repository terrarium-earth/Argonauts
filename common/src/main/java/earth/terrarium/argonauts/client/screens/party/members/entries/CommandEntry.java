package earth.terrarium.argonauts.client.screens.party.members.entries;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.components.selection.ListEntry;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import com.teamresourceful.resourcefullib.client.utils.CursorUtils;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.utils.ClientUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class CommandEntry extends ListEntry {

    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(Argonauts.MOD_ID, "textures/gui/party.png");

    private final Component text;
    private final Component buttonText;
    private final String command;
    private final boolean canEdit;

    public CommandEntry(Component text, Component buttonText, String command, boolean canEdit) {
        this.text = text;
        this.buttonText = buttonText;
        this.command = command;
        this.canEdit = canEdit;
    }

    public CommandEntry(Component text, Component buttonText, String command) {
        this(text, buttonText, command, true);
    }

    @Override
    protected void render(@NotNull ScissorBoxStack scissorStack, @NotNull PoseStack stack, int id, int left, int top, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick, boolean selected) {
        Font font = Minecraft.getInstance().font;

        font.draw(stack, this.text, left + 5, top + 5, canEdit ? 0xFFFFFF : 0x888888);

        boolean buttonHovered = mouseX >= left + width - 75 && mouseX < left + width - 5 && mouseY >= top + 4 && mouseY < top + 18;

        int offset = !canEdit ? 108 : buttonHovered ? 94 : 80;
        RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);
        Gui.blit(stack, left + width - 75, top + 4, 276, offset, 70, 14, 512, 512);

        font.draw(stack, this.buttonText, left + width - 75 + ((70 - font.width(this.buttonText)) / 2f), top + 7, canEdit ? 0xFFFFFF : 0x888888);
        if (buttonHovered) {
            CursorUtils.setCursor(true, canEdit ? CursorScreen.Cursor.POINTER : CursorScreen.Cursor.DISABLED);
        }
    }

    @Override
    public boolean mouseClicked(double d, double e, int i) {
        if (i == 0 && canEdit) {
            if (d >= 184 - 75 && d < 184 - 5 && e >= 4 && e < 18) {
                Minecraft.getInstance().player.closeContainer();
                ClientUtils.sendCommand(this.command);
                return true;
            }
        }
        return super.mouseClicked(d, e, i);
    }

    @Override
    public void setFocused(boolean bl) {}

    @Override
    public boolean isFocused() {
        return false;
    }
}
