package earth.terrarium.argonauts.common.handlers.party.members;

import com.mojang.authlib.GameProfile;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import earth.terrarium.argonauts.common.handlers.base.members.MemberState;
import earth.terrarium.argonauts.common.handlers.guild.members.GuildMember;
import earth.terrarium.argonauts.common.handlers.party.settings.PartySettings;

import java.util.Collection;
import java.util.Set;

public class PartyMember extends Member {

    private final PartySettings settings;

    public PartyMember(GameProfile profile, MemberState state) {
        super(profile, state);
        this.settings = new PartySettings();
    }

    public PartyMember(GameProfile profile, MemberState state, Set<String> permissions) {
        super(profile, state, permissions);
        this.settings = new PartySettings();
    }

    public PartyMember(GameProfile gameProfile, Collection<String> strings, MemberState memberState, String role, PartySettings settings) {
        super(gameProfile, strings, memberState, role);
        this.settings = settings;
    }

    public PartySettings settings() {
        return settings;
    }

    @Override
    public String getRole() {
        if (this.state.isLeader()) {
            return "Leader";
        }
        return role;
    }

    public static final ByteCodec<PartyMember> BYTE_CODEC = ObjectByteCodec.create(
        GuildMember.GAME_PROFILE_CODEC.fieldOf(PartyMember::profile),
        ByteCodec.STRING.setOf().fieldOf(PartyMember::permissions),
        ByteCodec.ofEnum(MemberState.class).fieldOf(PartyMember::getState),
        ByteCodec.STRING.fieldOf(PartyMember::getRole),
        PartySettings.BYTE_CODEC.fieldOf(PartyMember::settings),
        PartyMember::new
    );
}
