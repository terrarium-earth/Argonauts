package earth.terrarium.argonauts.common.handlers.guild.members;

import com.mojang.authlib.GameProfile;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.members.MemberState;
import earth.terrarium.argonauts.common.handlers.base.members.Members;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class GuildMembers extends Members<GuildMember> {
    private final Set<UUID> fakePlayers = new HashSet<>();

    public GuildMembers(GameProfile leader) {
        super(leader, GuildMember::new);
    }

    public GuildMembers(List<GuildMember> members, GameProfile leader, Set<UUID> fakePlayers) {
        super(leader, GuildMember::new);
        members.forEach(member -> this.members.put(member.profile().getId(), member));
        this.fakePlayers.addAll(fakePlayers);
    }

    @Override
    public void add(GameProfile profile) {
        if (this.members.containsKey(profile.getId())) {
            this.members.get(profile.getId()).setState(this.isLeader(profile.getId()) ? MemberState.OWNER : MemberState.MEMBER);
            return;
        }
        this.members.put(profile.getId(), new GuildMember(profile, MemberState.MEMBER));
    }

    @Override
    public void setLeader(GameProfile leader) throws MemberException {
        if (!isMember(leader.getId())) {
            throw MemberException.YOU_CANT_SET_OWNER_TO_NON_GUILD_MEMBER;
        }
        if (this.leader.equals(leader)) {
            throw MemberException.YOU_CANT_SET_OWNER_TO_CURRENT_OWNER;
        }
        forEach(member -> member.setState(MemberState.MEMBER));
        this.members.get(leader.getId()).setState(MemberState.OWNER);
        this.leader = leader;
    }

    public Set<UUID> fakePlayers() {
        return this.fakePlayers;
    }

    public static final ByteCodec<GuildMembers> BYTE_CODEC = ObjectByteCodec.create(
        GuildMember.BYTE_CODEC.listOf().fieldOf(GuildMembers::allMembers),
        GuildMember.GAME_PROFILE_CODEC.fieldOf(GuildMembers::leader),
        ByteCodec.UUID.setOf().fieldOf(GuildMembers::fakePlayers),
        GuildMembers::new
    );
}
