package earth.terrarium.argonauts.client.screens.base.members.entries;

import com.mojang.blaze3d.platform.InputConstants;
import com.teamresourceful.resourcefullib.client.components.selection.ListEntry;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import com.teamresourceful.resourcefullib.client.utils.CursorUtils;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.party.settings.PartySettingsScreen;
import earth.terrarium.argonauts.common.handlers.GroupType;
import earth.terrarium.argonauts.common.network.NetworkHandler;
import earth.terrarium.argonauts.common.network.messages.ServerboundSetPermissionPacket;
import earth.terrarium.argonauts.common.network.messages.ServerboundSetSettingPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Supplier;

public class BooleanEntry extends ListEntry {

    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(Argonauts.MOD_ID, "textures/gui/members.png");

    private final boolean canEdit;
    private final String prefix;
    private final String id;
    private final Supplier<GroupType> groupType;
    private final Supplier<UUID> getMember;
    private boolean value;

    public BooleanEntry(String id, boolean value, boolean canEdit, Supplier<GroupType> groupType, Supplier<UUID> getMember) {
        this("permission", id, value, canEdit, groupType, getMember);
    }

    public BooleanEntry(String prefix, String id, boolean value, boolean canEdit, Supplier<GroupType> groupType, Supplier<UUID> getMember) {
        this.prefix = prefix;
        this.id = id;
        this.value = value;
        this.canEdit = canEdit;
        this.groupType = groupType;
        this.getMember = getMember;
    }

    @Override
    protected void render(@NotNull GuiGraphics graphics, @NotNull ScissorBoxStack scissorStack, int id, int left, int top, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick, boolean selected) {
        graphics.drawString(
            Minecraft.getInstance().font,
            Component.translatable("argonauts." + this.prefix + "." + this.id),
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
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (button == InputConstants.MOUSE_BUTTON_LEFT && y >= 5 && y <= 17 && canEdit) {
            if (x > 156 && x < 179) {
                value = !value;
                if (prefix.equals("setting")) {
                    NetworkHandler.CHANNEL.sendToServer(new ServerboundSetSettingPacket(
                        id,
                        value,
                        getMember.get(),
                        Minecraft.getInstance().screen instanceof PartySettingsScreen s && s.menuContent().partySettings()));
                } else if (prefix.equals("permission")) {
                    NetworkHandler.CHANNEL.sendToServer(new ServerboundSetPermissionPacket(
                        id,
                        value,
                        groupType.get(),
                        getMember.get()));
                }
                return true;
            }
        }
        return super.mouseClicked(x, y, button);
    }

    @Override
    public void setFocused(boolean bl) {}

    @Override
    public boolean isFocused() {
        return false;
    }

}
