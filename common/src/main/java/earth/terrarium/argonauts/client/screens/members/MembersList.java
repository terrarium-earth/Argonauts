package earth.terrarium.argonauts.client.screens.members;

import com.teamresourceful.resourcefullib.client.components.selection.ListEntry;
import com.teamresourceful.resourcefullib.client.components.selection.SelectionList;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import com.teamresourceful.resourcefullib.client.utils.RenderUtils;
import com.teamresourceful.resourcefullib.client.utils.ScreenUtils;
import earth.terrarium.argonauts.Argonauts;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class MembersList extends SelectionList<MembersList.Entry> {

    private static final ResourceLocation CONTAINER_BACKGROUND = Argonauts.id("textures/gui/members.png");

    private Entry selected;

    public MembersList(int x, int y, int width, int height, int itemHeight, List<PlayerInfo> members, Consumer<@Nullable Entry> onSelection) {
        super(x, y, width, height, itemHeight, onSelection, true);
        update(members);
    }

    public void update(List<PlayerInfo> members) {
        updateEntries(members.stream().map(Entry::new).toList());
    }

    @Override
    public void setSelected(@Nullable Entry entry) {
        super.setSelected(entry);
        this.selected = entry;
    }

    public class Entry extends ListEntry {

        private final PlayerInfo member;
        private final ResourceLocation skin;

        public Entry(PlayerInfo member) {
            this.member = member;
            this.skin = member.getSkin().texture();
        }

        @Override
        protected void render(@NotNull GuiGraphics graphics, @NotNull ScissorBoxStack scissorStack, int id, int left, int top, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick, boolean selected) {
            graphics.blit(CONTAINER_BACKGROUND, left, top, 276, hovered ? 20 : 0, 70, 20, 512, 512);

            PlayerFaceRenderer.draw(graphics, this.skin, left + 2, top + 2, 16);

            try (var ignored = RenderUtils.createScissorBoxStack(scissorStack, Minecraft.getInstance(), graphics.pose(), left + 20, top + 2, width - 24, height - 4)) {
                graphics.drawString(
                    Minecraft.getInstance().font,
                    member.getProfile().getName(), left + 21, top + 5, 0xFFFFFF,
                    false
                );
            }

            if (hovered) {
                ScreenUtils.setTooltip(Component.literal(member.getProfile().getName()));
                if (Minecraft.getInstance().screen instanceof CursorScreen cursorScreen) {
                    cursorScreen.setCursor(CursorScreen.Cursor.POINTER);
                }
            }
        }

        @Override
        public void setFocused(boolean focused) {}

        @Override
        public boolean isFocused() {
            return this == selected;
        }

        public PlayerInfo playerInfo() {
            return this.member;
        }
    }
}
