package earth.terrarium.argonauts.api.teams.settings;

import earth.terrarium.argonauts.api.ApiHelper;
import earth.terrarium.argonauts.api.teams.Team;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface TeamSettingsApi {

    TeamSettingsApi API = ApiHelper.load(TeamSettingsApi.class);

    /**
     * Registers a new setting with a default value.
     *
     * @param defaultValue The value of the setting.
     */
    void register(Setting<?> defaultValue);

    /**
     * Adds the setting to all parties' available settings.
     *
     * @param setting The setting to add.
     * @throws IllegalArgumentException If the setting is already registered as a party setting.
     */
    void addPartySetting(Setting<?> setting);

    /**
     * Adds the setting to all guilds' available settings.
     *
     * @param setting The setting to add.
     * @throws IllegalArgumentException If the setting is already registered as a guild setting.
     */
    void addGuildSetting(Setting<?> setting);

    /**
     * Adds the setting to all guilds' and parties' available settings.
     *
     * @param setting The setting to add.
     * @throws IllegalArgumentException If the setting is already registered as a guild or party setting.
     */
    default void add(Setting<?> setting) {
        addPartySetting(setting);
        addGuildSetting(setting);
    }

    /**
     * Gets all settings that are available to parties.
     *
     * @return All party settings.
     */
    Map<String, Setting<?>> getPartySettings();

    /**
     * Gets all settings that are available to guilds.
     *
     * @return All guild settings.
     */
    Map<String, Setting<?>> getGuildSettings();

    /**
     * Gets the default value of a setting.
     *
     * @param id The id of the setting.
     * @return The default value of the setting.
     */
    Setting<?> getDefaultValue(String id);


    /**
     * Gets all registered setting names and their default values.
     *
     * @return All registered setting names and default values.
     */
    Map<String, Setting<?>> getAllDefaults();

    /**
     * Gets the value of a setting for the team
     *
     * @param team        The team.
     * @param settingName The name of the setting.
     * @return The value of the setting, or the setting's default value if the team hasn't overridden it.
     * @throws IllegalArgumentException If the setting is not registered.
     */
    @NotNull
    <T> Setting<T> getSetting(Team team, String settingName);
}
