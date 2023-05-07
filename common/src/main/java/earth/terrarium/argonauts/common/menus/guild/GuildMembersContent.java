package earth.terrarium.argonauts.common.menus.guild;

import com.mojang.authlib.GameProfile;
import com.teamresourceful.resourcefullib.common.menu.MenuContentSerializer;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import earth.terrarium.argonauts.common.handlers.base.members.MemberState;
import earth.terrarium.argonauts.common.handlers.guild.members.GuildMember;
import earth.terrarium.argonauts.common.menus.base.MembersContent;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class GuildMembersContent extends MembersContent {
    public GuildMembersContent(UUID id, int selected, List<? extends Member> members, boolean canManageMembers, boolean canManagePermissions) {
        super(id, selected, members, canManageMembers, canManagePermissions);
    }

    public static final MenuContentSerializer<MembersContent> SERIALIZER = new Serializer();

    @Override
    public MenuContentSerializer<MembersContent> serializer() {
        return SERIALIZER;
    }

    private static class Serializer extends MembersContent.Serializer {
        public Serializer() {
            super(GuildMembersContent::new);
        }

        @Override
        public Member createMember(GameProfile profile, MemberState state, Set<String> permissions) {
            return new GuildMember(profile, state, permissions);
        }
    }
}
