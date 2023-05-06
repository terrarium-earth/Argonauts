package earth.terrarium.argonauts.common.handlers.party.members;

import com.mojang.authlib.GameProfile;
import earth.terrarium.argonauts.common.handlers.base.members.MemberState;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import earth.terrarium.argonauts.common.handlers.party.settings.PartySettings;

import java.util.Set;

public class PartyMember extends Member {

    private final PartySettings settings = new PartySettings();

    public PartyMember(GameProfile profile, MemberState state) {
        super(profile, state);
    }

    public PartyMember(GameProfile profile, MemberState state, Set<String> permissions) {
        super(profile, state, permissions);
    }

    public PartySettings settings() {
        return settings;
    }
}
