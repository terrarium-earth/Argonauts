package earth.terrarium.argonauts.common.handlers.base.members;

import com.mojang.authlib.GameProfile;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public abstract class Members<T extends Member> implements Iterable<T> {
    protected final Map<UUID, T> members = new HashMap<>();
    protected UUID leader;
    private final Factory<T> factory;

    public Members(GameProfile leader, Factory<T> factory) {
        this.leader = leader.getId();
        this.members.put(leader.getId(), factory.createMember(leader, MemberState.OWNER));
        this.factory = factory;
    }

    @Nullable
    public T get(UUID uuid) {
        return this.members.get(uuid);
    }

    public abstract void add(GameProfile profile);

    public void invite(GameProfile profile) {
        this.members.put(profile.getId(), factory.createMember(profile, MemberState.INVITED));
    }

    public void remove(UUID uuid) {
        this.members.remove(uuid);
    }

    public abstract void setLeader(UUID uuid) throws MemberException;

    public T getLeader() {
        return this.members.get(this.leader);
    }

    public boolean isMember(UUID uuid) {
        return this.members.containsKey(uuid) && this.members.get(uuid).getState() != MemberState.INVITED;
    }

    public boolean isInvited(UUID uuid) {
        return this.members.containsKey(uuid) && this.members.get(uuid).getState() == MemberState.INVITED;
    }

    public boolean isLeader(UUID uuid) {
        return this.leader.equals(uuid);
    }

    public List<T> allMembers() {
        return new ArrayList<>(this.members.values());
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        List<T> members = new ArrayList<>(this.members.values());
        members.removeIf(member -> member.getState() == MemberState.INVITED);
        return members.iterator();
    }

    @FunctionalInterface
    public interface Factory<T extends Member> {
        T createMember(GameProfile profile, MemberState state);
    }
}
