package earth.terrarium.argonauts.client.screens.base;

import com.mojang.blaze3d.systems.RenderSystem;
import com.teamresourceful.resourcefullib.client.CloseablePoseStack;
import com.teamresourceful.resourcefullib.client.screens.BaseCursorScreen;
import com.teamresourceful.resourcefullib.common.menu.MenuContent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public abstract class BaseScreen<T extends MenuContent<T>> extends BaseCursorScreen {

    protected final int imageWidth;
    protected final int imageHeight;
    protected int leftPos;
    protected int topPos;
    protected final T menuContent;
    protected int titleLabelX;
    protected int titleLabelY;

    protected BaseScreen(T menuContent, Component displayName, int imageWidth, int imageHeight) {
        super(displayName);
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
        this.menuContent = menuContent;
        this.titleLabelX = 8;
        this.titleLabelY = 6;
    }

    @Override
    protected void init() {
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
    }

    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.renderBg(graphics, partialTick, mouseX, mouseY);
        RenderSystem.disableDepthTest();
        super.render(graphics, mouseX, mouseY, partialTick);
        try (var pose = new CloseablePoseStack(graphics)) {
            pose.translate(this.leftPos, this.topPos, 0.0F);
            this.renderLabels(graphics, mouseX, mouseY);
        }
        RenderSystem.enableDepthTest();
    }

    protected abstract void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY);

    protected abstract void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY);
}
