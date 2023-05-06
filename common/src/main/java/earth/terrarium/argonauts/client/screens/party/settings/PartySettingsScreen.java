package earth.terrarium.argonauts.client.screens.party.settings;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.screens.AbstractContainerCursorScreen;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.party.members.PartySettingList;
import earth.terrarium.argonauts.client.screens.party.members.entries.BooleanEntry;
import earth.terrarium.argonauts.client.screens.party.members.entries.CommandEntry;
import earth.terrarium.argonauts.client.screens.party.members.entries.DividerEntry;
import earth.terrarium.argonauts.client.utils.MouseLocationFix;
import earth.terrarium.argonauts.common.menus.party.PartySettingsMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class PartySettingsScreen extends AbstractContainerCursorScreen<PartySettingsMenu> {

    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(Argonauts.MOD_ID, "textures/gui/party_settings.png");

    public PartySettingsScreen(PartySettingsMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.passEvents = false;
        this.imageHeight = 220;
        this.imageWidth = 200;
    }

    @Override
    protected void init() {
        MouseLocationFix.fix(this.getClass());
        super.init();

        var list = addRenderableWidget(new PartySettingList(this.leftPos + 8, this.topPos + 18, 184, 180));
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
    public void render(@NotNull PoseStack stack, int i, int j, float f) {
        this.renderBackground(stack);
        super.render(stack, i, j, f);
        this.renderTooltip(stack, i, j);
    }

    @Override
    protected void renderLabels(@NotNull PoseStack stack, int i, int j) {
        this.font.draw(stack, title, (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
    }

    @Override
    protected void renderBg(@NotNull PoseStack stack, float f, int i, int j) {
        RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        blit(stack, k, l, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public void removed() {
        super.removed();
        MouseLocationFix.setFix(clas -> clas == PartySettingsScreen.class);
    }
}
