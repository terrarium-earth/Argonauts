package earth.terrarium.argonauts.common.menus.party;

import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import com.teamresourceful.resourcefullib.common.menu.MenuContent;
import com.teamresourceful.resourcefullib.common.menu.MenuContentSerializer;
import earth.terrarium.argonauts.common.handlers.base.members.MemberState;
import earth.terrarium.argonauts.common.handlers.party.members.PartyMember;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public record PartyMembersContent(UUID partyId,
                                  int selected,
                                  List<PartyMember> members,
                                  boolean canManageMembers,
                                  boolean canManagePermissions
) implements MenuContent<PartyMembersContent> {

    public static final MenuContentSerializer<PartyMembersContent> SERIALIZER = new Serializer();

    @Override
    public MenuContentSerializer<PartyMembersContent> serializer() {
        return SERIALIZER;
    }

    private static class Serializer implements MenuContentSerializer<PartyMembersContent> {

        @Override
        public PartyMembersContent from(FriendlyByteBuf buffer) {
            List<PartyMember> members = buffer.readList(buf -> {
                GameProfile profile = buf.readGameProfile();
                MemberState state = buf.readEnum(MemberState.class);
                Set<String> permissions = buf.readCollection(Sets::newHashSetWithExpectedSize, FriendlyByteBuf::readUtf);
                String role = buf.readUtf();
                PartyMember member = new PartyMember(profile, state, permissions);
                member.setRole(role);
                return member;
            });
            boolean removeMembers = buffer.readBoolean();
            boolean canChangePermissions = buffer.readBoolean();
            UUID partyId = buffer.readUUID();
            int selected = buffer.readInt();
            return new PartyMembersContent(partyId, selected, members, removeMembers, canChangePermissions);
        }

        @Override
        public void to(FriendlyByteBuf buffer, PartyMembersContent content) {
            buffer.writeCollection(content.members(), (buf, member) -> {
                buf.writeGameProfile(member.profile());
                buf.writeEnum(member.getState());
                buf.writeCollection(member.permissions(), FriendlyByteBuf::writeUtf);
                buf.writeUtf(member.getRole());
            });
            buffer.writeBoolean(content.canManageMembers());
            buffer.writeBoolean(content.canManagePermissions());
            buffer.writeUUID(content.partyId());
            buffer.writeInt(content.selected());
        }
    }
}
