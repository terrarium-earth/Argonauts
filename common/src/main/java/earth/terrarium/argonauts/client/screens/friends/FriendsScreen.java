package earth.terrarium.argonauts.client.screens.friends;

import com.mojang.authlib.GameProfile;
import com.teamresourceful.resourcefullib.common.color.ConstantColors;
import earth.terrarium.argonauts.client.screens.BaseScreen;
import earth.terrarium.argonauts.client.widgets.Panel;
import earth.terrarium.argonauts.client.widgets.ProfileWidgetRenderer;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import earth.terrarium.olympus.client.components.Widgets;
import earth.terrarium.olympus.client.components.renderers.WidgetRenderers;
import earth.terrarium.olympus.client.ui.UIConstants;
import earth.terrarium.olympus.client.ui.UIIcons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.LayoutSettings;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;

import java.util.UUID;

public class FriendsScreen extends BaseScreen {

    public FriendsScreen() {
        super(ConstantComponents.FRIENDS, 256, 224);
    }

    @Override
    protected void init() {
        super.init();

        var buttons = new FrameLayout(leftPos + 4, topPos + 19, 248, 18);

        buttons.addChild(Widgets.button(btn -> {
            btn.withRenderer(WidgetRenderers.textWithIcon(Component.literal("Add Friend"), UIIcons.USER_ADD).withTextLeftIconLeft().withGap(6).withIconSize(12).withPadding(2, 4, 4, 0));
            btn.withSize(font.width("Add Friend") + 22, 18);
        }), LayoutSettings::alignHorizontallyLeft);

        buttons.addChild(Widgets.button(btn -> {
            btn.withRenderer(WidgetRenderers.textWithIcon(Component.literal("Messages"), UIIcons.MESSAGE_DOT).withTextLeftIconLeft().withGap(6).withIconSize(12).withPadding(2, 4, 4, 0));
            btn.withSize(font.width("Messages") + 22, 18);
        }), LayoutSettings::alignHorizontallyRight);

        buttons.arrangeElements();
        buttons.visitWidgets(this::addRenderableWidget);

        addRenderableWidget(Widgets.list(layout -> {
            layout.withTexture(UIConstants.LIST_BG);
            layout.withContentMargin(2);
            layout.withSize(248, 180);
            layout.withPosition(leftPos + 4, topPos + 39);
            layout.withContents(content -> {
                content.withChild(Widgets.frame(frame -> {
                    frame.withTexture(UIConstants.LIST_ENTRY.get(true, false));
                    frame.withSize(244, 31);
                    frame.withContents(entryContent -> {
                        entryContent.addChild(Panel.create(panel -> {
                            panel.withRenderer(
                                new ProfileWidgetRenderer(new PlayerInfo(new GameProfile(UUID.fromString("78a06298-7263-433f-8433-b4fe19d116f9"), "CodexAdrian"), false))
                                    .withPadding(3)
                            );
                            panel.withTexture(null);
                            panel.withSize(244, 31);
                        }), LayoutSettings::alignHorizontallyLeft);
                        entryContent.addChild(Widgets.button(button -> {
                            button.withRenderer(WidgetRenderers.icon(UIIcons.USER_X).withColor(ConstantColors.white).withPadding(2, 2, 4, 2));
                            button.withTexture(UIConstants.DANGER_BUTTON);
                            button.withSize(16, 18);
                        }), settings -> settings.alignHorizontallyRight().paddingRight(3));
                        entryContent.addChild(Widgets.button(button -> {
                            button.withRenderer(WidgetRenderers.icon(UIIcons.CHAT).withPadding(2, 2, 4, 2));
                            button.withSize(16, 18);
                        }), settings -> settings.alignHorizontallyRight().paddingRight(21));
                    });
                }));
            });
        }));
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, 5, 5, 0xFFFFFFFF);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blitSprite(UIConstants.MODAL, leftPos, topPos, imageWidth, imageHeight);
        graphics.blitSprite(UIConstants.MODAL_HEADER, leftPos + 1, topPos + 1, imageWidth - 2, 15);
    }

    public static void open() {
        Minecraft.getInstance().tell(() -> Minecraft.getInstance().setScreen(new FriendsScreen()));
    }
}
