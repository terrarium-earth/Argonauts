package earth.terrarium.argonauts.common.handlers.base.members;

import com.mojang.authlib.GameProfile;

import java.util.HashSet;
import java.util.Set;

public abstract class Member {
    private final GameProfile profile;
    private final Set<String> permissions = new HashSet<>();
    private MemberState state;
    private String role = "Member";

    public Member(GameProfile profile, MemberState state) {
        this(profile, state, new HashSet<>());
    }

    public Member(GameProfile profile, MemberState state, Set<String> permissions) {
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

    public boolean hasPermission(String permission) {
        return this.state == MemberState.OWNER || this.permissions.contains(permission);
    }

    public MemberState getState() {
        return state;
    }

    public void setState(MemberState state) {
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
