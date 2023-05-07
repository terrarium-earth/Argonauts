package earth.terrarium.argonauts.client.screens.base.members;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.components.selection.ListEntry;
import com.teamresourceful.resourcefullib.client.components.selection.SelectionList;
import com.teamresourceful.resourcefullib.client.scissor.ScissorBoxStack;
import com.teamresourceful.resourcefullib.client.screens.CursorScreen;
import com.teamresourceful.resourcefullib.client.utils.RenderUtils;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.utils.ClientUtils;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Consumer;

public class MembersList extends SelectionList<MembersList.Entry> {

    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(Argonauts.MOD_ID, "textures/gui/members.png");

    private Entry selected;

    public MembersList(int x, int y, int width, int height, int itemHeight, Consumer<@Nullable Entry> onSelection) {
        super(x, y, width, height, itemHeight, onSelection, true);
    }

    public void update(List<? extends Member> members) {
        updateEntries(members.stream().map(Entry::new).toList());
    }

    @Override
    public void setSelected(@Nullable Entry entry) {
        super.setSelected(entry);
        this.selected = entry;
    }

    public class Entry extends ListEntry {

        private final Member member;
        private final ResourceLocation skin;

        public Entry(Member member) {
            this.member = member;
            this.skin = getPlayerSkin(member.profile());
        }

        @Override
        protected void render(@NotNull ScissorBoxStack scissorStack, @NotNull PoseStack stack, int id, int left, int top, int width, int height, int mouseX, int mouseY, boolean hovered, float partialTick, boolean selected) {
            RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);
            blit(stack, left, top, 276, hovered ? 20 : 0, 70, 20, 512, 512);

            RenderSystem.setShaderTexture(0, this.skin);
            PlayerFaceRenderer.draw(stack, left + 2, top + 2, 16);

            try (var ignored = RenderUtils.createScissorBoxStack(scissorStack, Minecraft.getInstance(), stack, left + 20, top + 2, width - 24, height - 4)) {
                Minecraft.getInstance().font.drawShadow(stack, member.profile().getName(), left + 21, top + 5, 0xFFFFFF);
            }

            if (hovered) {
                ClientUtils.setTooltip(Component.literal(member.profile().getName()));
                if (Minecraft.getInstance().screen instanceof CursorScreen cursorScreen) {
                    cursorScreen.setCursor(CursorScreen.Cursor.POINTER);
                }
            }
        }

        private static ResourceLocation getPlayerSkin(GameProfile profile) {
            if (Minecraft.getInstance().getConnection() != null) {
                for (PlayerInfo player : Minecraft.getInstance().getConnection().getOnlinePlayers()) {
                    if (ModUtils.areProfilesSame(player.getProfile(), profile)) {
                        return player.getSkinLocation();
                    }
                }
            }
            return profile.getId() == null ? DefaultPlayerSkin.getDefaultSkin() : DefaultPlayerSkin.getDefaultSkin(profile.getId());
        }

        @Override
        public void setFocused(boolean bl) {}

        @Override
        public boolean isFocused() {
            return this == selected;
        }

        public GameProfile profile() {
            return member.profile();
        }
    }

}
