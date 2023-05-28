package earth.terrarium.argonauts.client.screens.party.settings;

import com.teamresourceful.resourcefullib.client.screens.AbstractContainerCursorScreen;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.base.members.MemberSettingList;
import earth.terrarium.argonauts.client.screens.base.members.entries.BooleanEntry;
import earth.terrarium.argonauts.client.screens.base.members.entries.CommandEntry;
import earth.terrarium.argonauts.client.screens.base.members.entries.DividerEntry;
import earth.terrarium.argonauts.client.utils.MouseLocationFix;
import earth.terrarium.argonauts.common.menus.party.PartySettingsMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class PartySettingsScreen extends AbstractContainerCursorScreen<PartySettingsMenu> {

    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(Argonauts.MOD_ID, "textures/gui/party_settings.png");

    public PartySettingsScreen(PartySettingsMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.imageHeight = 220;
        this.imageWidth = 200;
    }

    @Override
    protected void init() {
        MouseLocationFix.fix(this.getClass());
        super.init();

        var list = addRenderableWidget(new MemberSettingList(this.leftPos + 8, this.topPos + 18, 184, 180));
        list.addEntry(new DividerEntry(Component.literal("Settings")));
        this.menu.settings().forEach((setting, value) ->
            list.addEntry(new BooleanEntry("setting", setting, value, true))
        );
        if (!this.menu.isPartyScreen()) {
            list.addEntry(new DividerEntry(Component.literal("Actions")));
            list.addEntry(new CommandEntry(Component.literal("Leave Party"), Component.literal("Leave"), "party leave"));
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int i, int j, float f) {
        this.renderBackground(graphics);
        super.render(graphics, i, j, f);
        this.renderTooltip(graphics, i, j);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics graphics, int i, int j) {
        graphics.drawString(
            font,
            this.title, this.titleLabelX, this.titleLabelY, 4210752,
            false
        );
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float f, int i, int j) {
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        graphics.blit(CONTAINER_BACKGROUND, k, l, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void removed() {
        super.removed();
        MouseLocationFix.setFix(clas -> clas == PartySettingsScreen.class);
    }
}
