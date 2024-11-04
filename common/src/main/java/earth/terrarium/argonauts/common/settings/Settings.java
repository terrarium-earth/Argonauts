package earth.terrarium.argonauts.common.settings;

import earth.terrarium.argonauts.api.teams.settings.Setting;
import earth.terrarium.argonauts.api.teams.settings.TeamSettingsApi;
import earth.terrarium.argonauts.api.teams.settings.types.BooleanSetting;
import earth.terrarium.argonauts.api.teams.settings.types.ColorSettings;
import earth.terrarium.argonauts.api.teams.settings.types.OptionalGlobalPosSetting;
import earth.terrarium.argonauts.api.teams.settings.types.StringSetting;
import earth.terrarium.olympus.client.constants.MinecraftColors;
import net.minecraft.ChatFormatting;

import java.util.Optional;

public class Settings {

    public static final StringSetting DISPLAY_NAME = register(new StringSetting("displayName", ""));
    public static final ColorSettings COLOR = register(new ColorSettings("color", MinecraftColors.WHITE));

    public static final BooleanSetting PUBLIC = register(new BooleanSetting("public", false));
    public static final BooleanSetting FRIENDLY_FIRE = register(new BooleanSetting("friendlyFire", true));
    public static final BooleanSetting ANNOUNCE_JOIN = register(new BooleanSetting("announceJoin", true));
    public static final BooleanSetting ANNOUNCE_LEAVE = register(new BooleanSetting("announceLeave", true));

    public static final BooleanSetting PASSIVE_TELEPORT = register(new BooleanSetting("passiveTeleport", true));

    public static final StringSetting MOTD = register(new StringSetting("motd", ""));
    public static final OptionalGlobalPosSetting HEADQUARTERS = register(new OptionalGlobalPosSetting("headquarters", Optional.empty()));

    public static <T extends Setting<?>> T register(T setting) {
        TeamSettingsApi.API.register(setting);
        return setting;
    }

    public static void init() {
        TeamSettingsApi.API.add(DISPLAY_NAME);
        TeamSettingsApi.API.add(COLOR);
        TeamSettingsApi.API.add(PUBLIC);
        TeamSettingsApi.API.add(FRIENDLY_FIRE);
        TeamSettingsApi.API.add(ANNOUNCE_JOIN);
        TeamSettingsApi.API.add(ANNOUNCE_LEAVE);
        TeamSettingsApi.API.add(PASSIVE_TELEPORT);

        TeamSettingsApi.API.addGuildSetting(MOTD);
        TeamSettingsApi.API.addGuildSetting(HEADQUARTERS);
    }
}
