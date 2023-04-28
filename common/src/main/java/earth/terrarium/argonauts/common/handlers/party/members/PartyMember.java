package earth.terrarium.argonauts.common.handlers.party.members;

import com.mojang.authlib.GameProfile;
import earth.terrarium.argonauts.common.handlers.party.settings.PartySettings;

import java.util.HashSet;
import java.util.Set;

public class PartyMember {

    private final GameProfile profile;
    private final Set<String> permissions = new HashSet<>();
    private final PartySettings settings = new PartySettings();
    private PartyMemberState state;
    private String role = "Member";

    public PartyMember(GameProfile profile, PartyMemberState state) {
        this(profile, state, new HashSet<>());
    }

    public PartyMember(GameProfile profile, PartyMemberState state, Set<String> permissions) {
        this.profile = profile;
        this.state = state;
        this.permissions.addAll(permissions);
    }

    public GameProfile profile() {
        return profile;
    }

    public Set<String> permissions() {
        return permissions;
    }

    public PartySettings settings() {
        return settings;
    }

    public boolean hasPermission(String permission) {
        return this.state == PartyMemberState.LEADER || this.permissions.contains(permission);
    }

    public PartyMemberState getState() {
        return state;
    }

    public void setState(PartyMemberState state) {
        this.state = state;
    }

    public String getRole() {
        if (this.state.isLeader()) {
            return "Leader";
        }
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
