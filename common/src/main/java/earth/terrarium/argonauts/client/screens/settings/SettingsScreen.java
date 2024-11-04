package earth.terrarium.argonauts.client.screens.settings;

import com.teamresourceful.resourcefullib.client.utils.ScreenUtils;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.teams.Team;
import earth.terrarium.argonauts.api.teams.guild.GuildApi;
import earth.terrarium.argonauts.api.teams.party.PartyApi;
import earth.terrarium.argonauts.api.teams.settings.Setting;
import earth.terrarium.argonauts.api.teams.settings.TeamSettingsApi;
import earth.terrarium.argonauts.api.teams.settings.types.BooleanSetting;
import earth.terrarium.argonauts.client.screens.BaseScreen;
import earth.terrarium.argonauts.client.screens.entries.BooleanEntry;
import earth.terrarium.argonauts.client.screens.entries.DividerEntry;
import earth.terrarium.argonauts.client.screens.entries.StringEntry;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;
import java.util.UUID;

public class SettingsScreen extends BaseScreen {

    private static final ResourceLocation CONTAINER_BACKGROUND = Argonauts.id("textures/gui/settings.png");

    private final Team team;
    private final Map<String, Setting<?>> settings;

    private final UUID selfId;

    public SettingsScreen(Component displayName, Team team, Map<String, Setting<?>> settings) {
        super(displayName, 200, 220);
        this.team = team;
        this.settings = settings;
        this.selfId = Minecraft.getInstance().getGameProfile().getId();
    }

    @Override
    protected void init() {
        super.init();

        SettingList list = addRenderableWidget(new SettingList(this.leftPos + 8, this.topPos + 18, 184, 180));
        list.addEntry(new DividerEntry(ConstantComponents.SETTINGS));

        this.settings.forEach((id, setting) -> {
            if (!setting.hidden()) {
                Component title = Component.translatable("setting.argonauts." + id);
                Component description = Component.translatable("setting.argonauts." + id + ".description");

                if (setting instanceof BooleanSetting) {
                    list.addEntry(new BooleanEntry(
                            title,
                            description,
                            TeamSettingsApi.API.<Boolean>getSetting(this.team, id).get(team),
                            team.canManageSettings(this.selfId),
                            newValue -> ScreenUtils.sendCommand("%s settings %s %s".formatted(team.type(), id, newValue))
                        )
                    );
                } else {
                    list.addEntry(new StringEntry(
                            title,
                            description,
                            TeamSettingsApi.API.getSetting(this.team, id).toStringCommand(),
                            team.canManageSettings(this.selfId),
                            newValue -> ScreenUtils.sendCommand("%s settings %s %s".formatted(team.type(), id, newValue))
                        )
                    );
                }
            }
        });
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, this.titleLabelX, this.titleLabelY, 0x404040, false);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        graphics.blit(CONTAINER_BACKGROUND, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (Minecraft.getInstance().options.keyInventory.matches(keyCode, scanCode)) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public static void openGuild() {
        GuildApi.API.getPlayerGuild(Minecraft.getInstance().player).ifPresent(guild ->
            Minecraft.getInstance().tell(() ->
                Minecraft.getInstance().setScreen(new SettingsScreen(ConstantComponents.GUILD_SETTINGS_TITLE, guild, TeamSettingsApi.API.getGuildSettings()))
            )
        );
    }

    public static void openParty() {
        PartyApi.API.getPlayerParty(Minecraft.getInstance().player).ifPresent(party ->
            Minecraft.getInstance().tell(() ->
                Minecraft.getInstance().setScreen(new SettingsScreen(ConstantComponents.PARTY_SETTINGS_TITLE, party, TeamSettingsApi.API.getPartySettings()))
            )
        );
    }
}
