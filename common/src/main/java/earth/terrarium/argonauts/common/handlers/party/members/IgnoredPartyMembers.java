package earth.terrarium.argonauts.common.handlers.party.members;

import com.mojang.authlib.GameProfile;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class IgnoredPartyMembers {

    public IgnoredPartyMembers() {}

    public IgnoredPartyMembers(Set<UUID> ignored) {
        this.ignored.addAll(ignored);
    }

    private final Set<UUID> ignored = new HashSet<>();

    public boolean has(UUID uuid) {
        return this.ignored.contains(uuid);
    }

    public void add(GameProfile profile) {
        this.ignored.add(profile.getId());
    }

    public void remove(GameProfile profile) {
        this.ignored.remove(profile.getId());
    }

    public void clear() {
        this.ignored.clear();
    }

    public Set<UUID> getIgnored() {
        return this.ignored;
    }
}
