package earth.terrarium.argonauts.common.handlers.guild.members;

import com.mojang.authlib.GameProfile;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import earth.terrarium.argonauts.common.handlers.base.members.MemberState;

import java.util.Collection;
import java.util.Set;

public class GuildMember extends Member {
    public GuildMember(GameProfile profile, MemberState state) {
        super(profile, state);
    }

    public GuildMember(GameProfile profile, MemberState state, Set<String> permissions) {
        super(profile, state, permissions);
    }

    public GuildMember(GameProfile gameProfile, Collection<String> strings, MemberState memberState, String role) {
        super(gameProfile, strings, memberState, role);
    }

    @Override
    public String getRole() {
        if (this.state.isLeader()) {
            return "Owner";
        }
        return role;
    }

    public static final ByteCodec<GameProfile> GAME_PROFILE_CODEC = ObjectByteCodec.create(
        ByteCodec.UUID.fieldOf(GameProfile::getId),
        ByteCodec.STRING.fieldOf(GameProfile::getName),
        GameProfile::new
    );

    public static final ByteCodec<GuildMember> BYTE_CODEC = ObjectByteCodec.create(
        GAME_PROFILE_CODEC.fieldOf(GuildMember::profile),
        ByteCodec.STRING.setOf().fieldOf(Member::permissions),
        ByteCodec.ofEnum(MemberState.class).fieldOf(GuildMember::getState),
        ByteCodec.STRING.fieldOf(GuildMember::getRole),
        GuildMember::new
    );
}
