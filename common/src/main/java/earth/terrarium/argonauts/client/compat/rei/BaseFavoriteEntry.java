package earth.terrarium.argonauts.client.compat.rei;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.favorites.FavoriteEntry;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;

@SuppressWarnings("UnstableApiUsage")
public abstract class BaseFavoriteEntry extends FavoriteEntry {

    private final ResourceLocation id;
    private final ResourceLocation texture;
    private final Runnable action;

    public BaseFavoriteEntry(ResourceLocation id, ResourceLocation texture, Runnable action) {
        this.id = id;
        this.texture = texture;
        this.action = action;
    }

    @Override
    public boolean isInvalid() {
        return false;
    }

    @Override
    public Renderer getRenderer(boolean showcase) {
        return new Renderer() {
            @Override
            public void render(GuiGraphics graphics, Rectangle bounds, int mouseX, int mouseY, float delta) {
                graphics.pose().pushPose();
                graphics.pose().translate(bounds.getCenterX(), bounds.getCenterY(), 0);
                graphics.pose().scale(bounds.getWidth() / 16f, bounds.getHeight() / 16f, 1);
                graphics.blit(texture, -8, -8, 0, 0, 16, 16, 16, 16);
                graphics.pose().popPose();
            }

            @Override
            public Tooltip getTooltip(TooltipContext context) {
                return Tooltip.create(context.getPoint(), Component.translatable(id.toLanguageKey("rei", "tooltip")));
            }
        };
    }

    @Override
    public boolean doAction(int button) {
        if (button == 0) {
            Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            action.run();
            return true;
        }
        return false;
    }

    @Override
    public long hashIgnoreAmount() {
        return 31290831290L;
    }

    @Override
    public FavoriteEntry copy() {
        return this;
    }

    @Override
    public ResourceLocation getType() {
        return id;
    }

    @Override
    public boolean isSame(FavoriteEntry other) {
        return other instanceof BaseFavoriteEntry;
    }
}