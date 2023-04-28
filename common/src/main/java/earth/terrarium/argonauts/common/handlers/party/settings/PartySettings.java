package earth.terrarium.argonauts.common.handlers.party.settings;

import java.util.LinkedHashSet;
import java.util.Set;

public record PartySettings(Set<String> settings) {

    public PartySettings() {
        this(new LinkedHashSet<>());
    }

    public boolean has(String setting) {
        return settings.contains(setting);
    }

    public void set(String setting, boolean value) {
        if (value) {
            settings.add(setting);
        } else {
            settings.remove(setting);
        }
    }
}
