package earth.terrarium.argonauts.common.handlers.guild.members;

import com.mojang.authlib.GameProfile;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.members.MemberState;
import earth.terrarium.argonauts.common.handlers.base.members.Members;

import java.util.UUID;

public class GuildMembers extends Members<GuildMember> {

    public GuildMembers(GameProfile leader) {
        super(leader, GuildMember::new);
    }

    @Override
    public void add(GameProfile profile) {
        if (this.members.containsKey(profile.getId())) {
            this.members.get(profile.getId()).setState(MemberState.MEMBER);
            return;
        }
        this.members.put(profile.getId(), new GuildMember(profile, MemberState.MEMBER));
    }

    @Override
    public void setLeader(UUID uuid) throws MemberException {
        if (!isMember(uuid)) {
            throw new MemberException("Cannot set owner to a member that is not in the guild");
        }
        if (this.leader.equals(uuid)) {
            throw new MemberException("Cannot set owner to the current owner");
        }
        forEach(member -> member.setState(MemberState.MEMBER));
        this.members.get(uuid).setState(MemberState.OWNER);
        this.leader = uuid;
    }
}
