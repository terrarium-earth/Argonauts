package earth.terrarium.argonauts.common.handlers.party.members;

import com.mojang.authlib.GameProfile;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.members.MemberState;
import earth.terrarium.argonauts.common.handlers.base.members.Members;
import net.minecraft.Optionull;

import java.util.UUID;

public class PartyMembers extends Members<PartyMember> {

    public PartyMembers(GameProfile leader) {
        super(leader, PartyMember::new);
    }

    @Override
    public void add(GameProfile profile) {
        if (Optionull.map(this.members.get(profile.getId()), PartyMember::getState) != MemberState.OWNER) {
            if (this.members.containsKey(profile.getId())) {
                this.members.get(profile.getId()).setState(MemberState.MEMBER);
                return;
            }
            this.members.put(profile.getId(), new PartyMember(profile, MemberState.MEMBER));
        }
    }

    @Override
    public void setLeader(UUID uuid) throws MemberException {
        if (!isMember(uuid)) {
            throw MemberException.YOU_CANT_SET_LEADER_TO_NON_PARTY_MEMBER;
        }
        if (this.leader.equals(uuid)) {
            throw MemberException.YOU_CANT_SET_LEADER_TO_CURRENT_LEADER;
        }
        forEach(member -> member.setState(MemberState.MEMBER));
        this.members.get(uuid).setState(MemberState.OWNER);
        this.leader = uuid;
    }
}