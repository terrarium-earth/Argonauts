package earth.terrarium.argonauts.common.handlers.party.members;

import com.mojang.authlib.GameProfile;
import earth.terrarium.argonauts.common.handlers.MemberState;
import earth.terrarium.argonauts.common.handlers.party.PartyException;
import net.minecraft.Optionull;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PartyMembers implements Iterable<PartyMember> {

    private final Map<UUID, PartyMember> members = new HashMap<>();
    private UUID leader;

    public PartyMembers(GameProfile leader) {
        this.leader = leader.getId();
        this.members.put(leader.getId(), new PartyMember(leader, MemberState.OWNER));
    }

    public PartyMember get(UUID uuid) {
        return this.members.get(uuid);
    }

    public void add(GameProfile profile) {
        if (Optionull.map(this.members.get(profile.getId()), PartyMember::getState) != MemberState.OWNER) {
            if (this.members.containsKey(profile.getId())) {
                this.members.get(profile.getId()).setState(MemberState.MEMBER);
                return;
            }
            this.members.put(profile.getId(), new PartyMember(profile, MemberState.MEMBER));
        }
    }

    public void invite(GameProfile profile) {
        this.members.put(profile.getId(), new PartyMember(profile, MemberState.INVITED));
    }

    public void remove(UUID uuid) {
        this.members.remove(uuid);
    }

    public void setLeader(UUID uuid) throws PartyException {
        if (!isMember(uuid)) {
            throw new PartyException("Cannot set leader to a member that is not in the party");
        }
        if (this.leader.equals(uuid)) {
            throw new PartyException("Cannot set leader to the current leader");
        }
        forEach(member -> member.setState(MemberState.MEMBER));
        this.members.get(uuid).setState(MemberState.OWNER);
        this.leader = uuid;
    }

    public PartyMember getLeader() {
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

    public List<PartyMember> allMembers() {
        return new ArrayList<>(this.members.values());
    }

    @NotNull
    @Override
    public Iterator<PartyMember> iterator() {
        List<PartyMember> members = new ArrayList<>(this.members.values());
        members.removeIf((member) -> member.getState() == MemberState.INVITED);
        return members.iterator();
    }
}
