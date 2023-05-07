package earth.terrarium.argonauts.common.menus.party;

import com.mojang.authlib.GameProfile;
import com.teamresourceful.resourcefullib.common.menu.MenuContentSerializer;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import earth.terrarium.argonauts.common.handlers.base.members.MemberState;
import earth.terrarium.argonauts.common.handlers.party.members.PartyMember;
import earth.terrarium.argonauts.common.menus.base.MembersContent;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PartyMembersContent extends MembersContent {
    public PartyMembersContent(UUID id, int selected, List<? extends Member> members, boolean canManageMembers, boolean canManagePermissions) {
        super(id, selected, members, canManageMembers, canManagePermissions);
    }

    public static final MenuContentSerializer<MembersContent> SERIALIZER = new Serializer();

    @Override
    public MenuContentSerializer<MembersContent> serializer() {
        return SERIALIZER;
    }

    private static class Serializer extends MembersContent.Serializer {
        public Serializer() {
            super(PartyMembersContent::new);
        }

        @Override
        public Member createMember(GameProfile profile, MemberState state, Set<String> permissions) {
            return new PartyMember(profile, state, permissions);
        }
    }
}
