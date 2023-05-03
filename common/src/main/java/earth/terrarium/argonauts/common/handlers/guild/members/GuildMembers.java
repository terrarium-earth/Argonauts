package earth.terrarium.argonauts.common.handlers.guild.members;

import com.mojang.authlib.GameProfile;
import earth.terrarium.argonauts.common.handlers.MemberState;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GuildMembers implements Iterable<GuildMember> {

    private final Map<UUID, GuildMember> members = new HashMap<>();
    private UUID owner;

    public GuildMembers(GameProfile owner) {
        this.owner = owner.getId();
        this.members.put(owner.getId(), new GuildMember(owner, MemberState.OWNER));
    }

    public GuildMember get(UUID uuid) {
        return this.members.get(uuid);
    }

    public void add(GameProfile profile) {
        if (this.members.containsKey(profile.getId())) {
            this.members.get(profile.getId()).setState(MemberState.MEMBER);
            return;
        }
        this.members.put(profile.getId(), new GuildMember(profile, MemberState.MEMBER));
    }

    public void invite(GameProfile profile) {
        this.members.put(profile.getId(), new GuildMember(profile, MemberState.INVITED));
    }

    public void remove(UUID uuid) {
        this.members.remove(uuid);
    }

    public void setOwner(UUID uuid) {
        if (!isMember(uuid)) {
            throw new RuntimeException("Cannot set owner to a member that is not in the guild");
        }
        if (this.owner.equals(uuid)) {
            throw new RuntimeException("Cannot set owner to the current owner");
        }
        forEach(member -> member.setState(MemberState.MEMBER));
        this.members.get(uuid).setState(MemberState.OWNER);
        this.owner = uuid;
    }

    public GuildMember getOwner() {
        return this.members.get(this.owner);
    }

    public boolean isMember(UUID uuid) {
        return this.members.containsKey(uuid) && this.members.get(uuid).getState() != MemberState.INVITED;
    }

    public boolean isInvited(UUID uuid) {
        return this.members.containsKey(uuid) && this.members.get(uuid).getState() == MemberState.INVITED;
    }

    public boolean isOwner(UUID uuid) {
        return this.owner.equals(uuid);
    }

    public List<GuildMember> allMembers() {
        return new ArrayList<>(this.members.values());
    }

    @NotNull
    @Override
    public Iterator<GuildMember> iterator() {
        List<GuildMember> members = new ArrayList<>(this.members.values());
        members.removeIf(member -> member.getState() == MemberState.INVITED);
        return members.iterator();
    }
}
