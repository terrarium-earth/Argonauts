package earth.terrarium.argonauts.common.handlers.guild.members;

import com.mojang.authlib.GameProfile;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import earth.terrarium.argonauts.common.handlers.base.members.MemberState;

import java.util.Set;

public class GuildMember extends Member {
    public GuildMember(GameProfile profile, MemberState state) {
        super(profile, state);
    }

    public GuildMember(GameProfile profile, MemberState state, Set<String> permissions) {
        super(profile, state, permissions);
    }
}
