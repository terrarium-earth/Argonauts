package earth.terrarium.argonauts.client.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import earth.terrarium.olympus.client.components.base.BaseWidget;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRenderer;
import earth.terrarium.olympus.client.components.base.renderer.WidgetRendererContext;
import earth.terrarium.olympus.client.ui.UIConstants;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class Panel extends BaseWidget {

    private WidgetRenderer<? super Panel> renderer = WidgetRenderer.empty();
    @Nullable
    private ResourceLocation sprite = UIConstants.BUTTON.get(true, false);

    public Panel() {
        super();
    }

    public static Panel create(Consumer<Panel> config) {
        Panel panel = new Panel();
        config.accept(panel);
        return panel;
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        graphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();

        if (this.sprite != null) {
            graphics.blitSprite(
                this.sprite,
                this.getX(), this.getY(),
                this.getWidth(), this.getHeight()
            );
        }

        this.renderer.render(graphics, new WidgetRendererContext<>(this, mouseX, mouseY), partialTick);
    }

    public Panel withRenderer(WidgetRenderer<? super Panel> renderer) {
        this.renderer = renderer;
        return this;
    }

    public Panel withTexture(@Nullable ResourceLocation sprite) {
        this.sprite = sprite;
        return this;
    }

    @Override
    public Panel withSize(int width, int height) {
        return (Panel) super.withSize(width, height);
    }
}
