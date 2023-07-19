package earth.terrarium.argonauts.client.screens.base.members.entries;

import com.mojang.blaze3d.platform.InputConstants;
import com.teamresourceful.resourcefullib.client.components.selection.ListEntry;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import com.teamresourceful.resourcefullib.client.utils.CursorUtils;
import com.teamresourceful.resourcefullib.client.utils.RenderUtils;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.common.handlers.GroupType;
import earth.terrarium.argonauts.common.network.NetworkHandler;
import earth.terrarium.argonauts.common.network.messages.ServerboundSetRolePacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Supplier;

public class RoleNameEntry extends ListEntry {

    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(Argonauts.MOD_ID, "textures/gui/members.png");

    private final boolean canEdit;
    private final Supplier<GroupType> groupType;
    private final Supplier<UUID> getMember;
    private String ogText = "";
    private String text = "";

    public RoleNameEntry(boolean canEdit, Supplier<GroupType> groupType, Supplier<UUID> getMember) {
        this.canEdit = canEdit;
        this.groupType = groupType;
        this.getMember = getMember;
    }

    public void setText(String text) {
        this.text = text;
        this.ogText = text;
    }

    @Override
    protected void render(@NotNull GuiGraphics graphics, @NotNull ScissorBoxStack scissorStack, int id, int left, int top, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick, boolean selected) {
        graphics.drawString(
            Minecraft.getInstance().font,
            "Role Name", left + 5, top + (int) (height / 2f) - (int) (Minecraft.getInstance().font.lineHeight / 2f), canEdit ? 0xFFFFFFFF : 0xFF808080,
            false
        );
        int borderColor = selected && canEdit ? 0xFFFFFFFF : 0xFF808080;

        graphics.fill(left + width - 5 - 102, top + 2, left + width - 25, top + height - 2, borderColor);
        graphics.fill(left + width - 5 - 101, top + 3, left + width - 26, top + height - 3, 0xFF000000);

        try (var ignored = RenderUtils.createScissorBoxStack(scissorStack, Minecraft.getInstance(), graphics.pose(), left + width - 5 - 101, top + 3, 80, height - 4)) {
            graphics.drawString(
                Minecraft.getInstance().font,
                text, left + width - 5 - 99, top + 6, canEdit ? 0xFFFFFF : 0x505050,
                false
            );
        }

        boolean buttonHovered = hovered && mouseX >= left + width - 22 && mouseX < left + width - 6 && mouseY >= top + 2 && mouseY <= top + height - 2;

        int offset = !canEdit || text.equals(ogText) ? 32 : buttonHovered ? 16 : 0;
        graphics.blit(CONTAINER_BACKGROUND, left + width - 22, top + 2, 346, offset, 16, 16, 512, 512);

        if (hovered && mouseX >= left + width - 5 - 101 && mouseX < left + width - 25 && mouseY >= top + 3 && mouseY <= top + height - 3) {
            CursorUtils.setCursor(true, canEdit ? CursorScreen.Cursor.TEXT : CursorScreen.Cursor.DISABLED);
        } else if (buttonHovered) {
            CursorUtils.setCursor(true, canEdit && !text.equals(ogText) ? CursorScreen.Cursor.POINTER : CursorScreen.Cursor.DISABLED);
        }
    }

    @Override
    public boolean charTyped(char c, int i) {
        if (canEdit) {
            text += c;
            return true;
        }
        return false;
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        if (canEdit) {
            if (i == 259 && !text.isEmpty()) {
                text = text.substring(0, text.length() - 1);
                return true;
            }
            if (Screen.isPaste(i)) {
                String text = Minecraft.getInstance().keyboardHandler.getClipboard();
                this.text += text;
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean mouseClicked(double x, double y, int i) {
        if (i == InputConstants.MOUSE_BUTTON_LEFT && canEdit && !ogText.equals(text)) {
            if (x >= 184 - 22 && x < 184 - 6 && y >= 2 && y <= 20 - 2) {
                ogText = text;
                NetworkHandler.CHANNEL.sendToServer(new ServerboundSetRolePacket(
                    text,
                    this.groupType.get(),
                    this.getMember.get()));
                return true;
            }
        }
        return super.mouseClicked(x, y, i);
    }

    @Override
    public void setFocused(boolean bl) {}

    @Override
    public boolean isFocused() {
        return false;
    }
}
