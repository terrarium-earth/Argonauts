package earth.terrarium.argonauts.common.settings;

import earth.terrarium.argonauts.api.teams.Team;
import earth.terrarium.argonauts.api.teams.settings.Setting;
import earth.terrarium.argonauts.api.teams.settings.TeamSettingsApi;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TeamSettingsApiImpl implements TeamSettingsApi {

    private static final Map<String, Setting<?>> SETTINGS = new HashMap<>();
    private static final Map<String, Setting<?>> PARTY_SETTINGS = new HashMap<>();
    private static final Map<String, Setting<?>> GUILD_SETTINGS = new HashMap<>();

    @Override
    public void register(Setting<?> defaultValue) {
        SETTINGS.put(defaultValue.id(), defaultValue);
    }

    @Override
    public void addPartySetting(Setting<?> setting) {
        if (PARTY_SETTINGS.containsKey(setting.id())) {
            throw new IllegalArgumentException("Setting is already registered as a party setting.");
        }
        PARTY_SETTINGS.put(setting.id(), setting);
    }

    @Override
    public void addGuildSetting(Setting<?> setting) {
        if (GUILD_SETTINGS.containsKey(setting.id())) {
            throw new IllegalArgumentException("Setting is already registered as a guild setting.");
        }
        GUILD_SETTINGS.put(setting.id(), setting);
    }

    @Override
    public Map<String, Setting<?>> getPartySettings() {
        return PARTY_SETTINGS;
    }

    @Override
    public Map<String, Setting<?>> getGuildSettings() {
        return GUILD_SETTINGS;
    }

    @Override
    public Setting<?> getDefaultValue(String id) {
        return SETTINGS.get(id);
    }

    @Override
    public Map<String, Setting<?>> getAllDefaults() {
        return SETTINGS;
    }

    @Override
    @NotNull
    @SuppressWarnings("unchecked")
    public <T> Setting<T> getSetting(Team team, String settingName) {
        if (!SETTINGS.containsKey(settingName)) throw new IllegalArgumentException("Setting not registered: " + settingName);
        return (Setting<T>) team.settings().getOrDefault(settingName, getDefaultValue(settingName));
    }
}
