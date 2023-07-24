package earth.terrarium.argonauts.client.screens.party.settings;

import com.teamresourceful.resourcefullib.client.utils.MouseLocationFix;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.base.BaseScreen;
import earth.terrarium.argonauts.client.screens.base.members.MemberSettingList;
import earth.terrarium.argonauts.client.screens.base.members.entries.BooleanEntry;
import earth.terrarium.argonauts.client.screens.base.members.entries.CommandEntry;
import earth.terrarium.argonauts.client.screens.base.members.entries.DividerEntry;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import earth.terrarium.argonauts.common.handlers.GroupType;
import earth.terrarium.argonauts.common.menus.party.PartySettingsContent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PartySettingsScreen extends BaseScreen<PartySettingsContent> {

    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(Argonauts.MOD_ID, "textures/gui/party_settings.png");

    public PartySettingsScreen(PartySettingsContent menuContent, Component displayName) {
        super(menuContent, displayName, 200, 220);
    }

    @Override
    protected void init() {
        MouseLocationFix.fix(this.getClass());
        super.init();

        var list = addRenderableWidget(new MemberSettingList(this.leftPos + 8, this.topPos + 18, 184, 180));
        list.addEntry(new DividerEntry(ConstantComponents.SETTINGS));
        this.menuContent.settings().forEach((setting, value) ->
            list.addEntry(new BooleanEntry("setting", setting, value, true, () -> GroupType.PARTY, () -> Objects.requireNonNull(Minecraft.getInstance().player).getUUID())));
        if (!this.menuContent.partySettings()) {
            list.addEntry(new DividerEntry(ConstantComponents.ACTIONS));
            list.addEntry(new CommandEntry(ConstantComponents.LEAVE_PARTY, ConstantComponents.LEAVE, "party leave"));
        }
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int i, int j, float f) {
        this.renderBackground(graphics);
        super.render(graphics, i, j, f);
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

    public static void open(PartySettingsContent menuContent, Component displayName) {
        Minecraft.getInstance().setScreen(new PartySettingsScreen(menuContent, displayName));
    }
}
