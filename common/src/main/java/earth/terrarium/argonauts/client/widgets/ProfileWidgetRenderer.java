package earth.terrarium.argonauts.client.widgets;

import earth.terrarium.argonauts.common.constants.ConstantComponents;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRendererContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.resources.PlayerSkin;

import java.util.function.Supplier;

public class ProfileWidgetRenderer implements WidgetRenderer<AbstractWidget> {

    private final Supplier<PlayerSkin> skinGetter;
    private final PlayerInfo gameProfile;
    private int padding = 0;
    int avatarSize = 24;
    int statusDotSize = 5;

    public ProfileWidgetRenderer(PlayerInfo profile) {
        this.gameProfile = profile;
        this.skinGetter = profile::getSkin;
    }

    public ProfileWidgetRenderer withPadding(int padding) {
        this.padding = padding;
        return this;
    }

    public ProfileWidgetRenderer withAvatarSize(int faceSize) {
        this.avatarSize = faceSize;
        return this;
    }

    public ProfileWidgetRenderer withStatusDotSize(int statusDotSize) {
        this.statusDotSize = statusDotSize;
        return this;
    }

    @Override
    public void render(GuiGraphics graphics, WidgetRendererContext<AbstractWidget> ctx, float v) {
        var font = Minecraft.getInstance().font;
        int faceX = ctx.getX() + padding;
        int faceY = ctx.getY() + padding;

        PlayerFaceRenderer.draw(graphics, skinGetter.get(), faceX, faceY, avatarSize);

        boolean online = Minecraft.getInstance().getConnection() != null
            && Minecraft.getInstance().getConnection().getPlayerInfo(gameProfile.getProfile().getId()) != null;
        int dotColor = online ? 0xFF55FF55 : 0xFF888888;

        graphics.fill(faceX + avatarSize - statusDotSize, faceY + avatarSize - statusDotSize, faceX + avatarSize, faceY + avatarSize, 0x44888888);
        graphics.fill(faceX + avatarSize - statusDotSize + 1, faceY + avatarSize - statusDotSize + 1, faceX + avatarSize - 1, faceY + avatarSize - 1, dotColor);

        int textX = faceX + avatarSize + 4;
        int totalTextH = font.lineHeight * 2 + 2;
        int startY = ctx.getY() + (ctx.getHeight() - totalTextH) / 2;

        if (gameProfile.getTabListDisplayName() != null) {
            graphics.drawString(font, gameProfile.getTabListDisplayName(), textX, startY, 0xFFFFFFFF, false);
        } else {
            graphics.drawString(font, gameProfile.getProfile().getName(), textX, startY, 0xFFFFFFFF, false);
        }

        graphics.drawString(font, online ? ConstantComponents.ONLINE : ConstantComponents.OFFLINE, textX, startY + font.lineHeight + 2, dotColor, false);
    }
}
