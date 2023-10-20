package earth.terrarium.argonauts.common.handlers.party.members;

import com.mojang.authlib.GameProfile;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.members.MemberState;
import earth.terrarium.argonauts.common.handlers.base.members.Members;
import earth.terrarium.argonauts.common.handlers.guild.members.GuildMember;
import net.minecraft.Optionull;

import java.util.List;

public class PartyMembers extends Members<PartyMember> {

    public PartyMembers(GameProfile leader) {
        super(leader, PartyMember::new);
    }

    public PartyMembers(List<PartyMember> members, GameProfile leader) {
        super(leader, PartyMember::new);
        members.forEach(member -> this.members.put(member.profile().getId(), member));
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
    public void setLeader(GameProfile leader) throws MemberException {
        if (!isMember(leader.getId())) {
            throw MemberException.YOU_CANT_SET_LEADER_TO_NON_PARTY_MEMBER;
        }
        if (this.leader.equals(leader)) {
            throw MemberException.YOU_CANT_SET_LEADER_TO_CURRENT_LEADER;
        }
        forEach(member -> member.setState(MemberState.MEMBER));
        this.members.get(leader.getId()).setState(MemberState.OWNER);
        this.leader = leader;
    }

    public static final ByteCodec<PartyMembers> BYTE_CODEC = ObjectByteCodec.create(
        PartyMember.BYTE_CODEC.listOf().fieldOf(PartyMembers::allMembers),
        GuildMember.GAME_PROFILE_CODEC.fieldOf(PartyMembers::leader),
        PartyMembers::new
    );
}