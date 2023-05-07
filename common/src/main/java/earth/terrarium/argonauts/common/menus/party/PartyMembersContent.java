package earth.terrarium.argonauts.common.menus.party;

import com.teamresourceful.resourcefullib.common.menu.MenuContentSerializer;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import earth.terrarium.argonauts.common.handlers.party.members.PartyMember;
import earth.terrarium.argonauts.common.menus.base.MembersContent;

import java.util.List;
import java.util.UUID;

public class PartyMembersContent extends MembersContent {
    public PartyMembersContent(UUID id, int selected, List<? extends Member> members, boolean canManageMembers, boolean canManagePermissions) {
        super(id, selected, members, canManageMembers, canManagePermissions);
    }

    public static final MenuContentSerializer<MembersContent> SERIALIZER = new MembersContent.Serializer(PartyMembersContent::new, PartyMember::new);

    @Override
    public MenuContentSerializer<MembersContent> serializer() {
        return SERIALIZER;
    }
}
