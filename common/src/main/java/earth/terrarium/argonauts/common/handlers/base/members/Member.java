package earth.terrarium.argonauts.common.handlers.base.members;

import com.mojang.authlib.GameProfile;

import java.util.HashSet;
import java.util.Set;

public abstract class Member {
    protected final GameProfile profile;
    protected final Set<String> permissions = new HashSet<>();
    protected MemberState state;
    protected String role = "Member";

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

    public abstract String getRole();

    public void setRole(String role) {
        this.role = role;
    }
}
