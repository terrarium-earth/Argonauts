package earth.terrarium.argonauts.common.menus.base;

import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import com.teamresourceful.resourcefullib.common.menu.MenuContent;
import com.teamresourceful.resourcefullib.common.menu.MenuContentSerializer;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import earth.terrarium.argonauts.common.handlers.base.members.MemberState;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;

import java.util.*;

public abstract class MembersContent implements MenuContent<MembersContent> {
    private final UUID id;
    private final int selected;
    private final List<? extends Member> members;
    private final boolean canManageMembers;
    private final boolean canManagePermissions;

    public MembersContent(UUID id, int selected, List<? extends Member> members, boolean canManageMembers, boolean canManagePermissions) {
        this.id = id;
        this.selected = selected;
        this.members = members;
        this.canManageMembers = canManageMembers;
        this.canManagePermissions = canManagePermissions;
    }

    public UUID id() {return id;}

    public int selected() {return selected;}

    public List<? extends Member> members() {return members;}

    public boolean canManageMembers() {return canManageMembers;}

    public boolean canManagePermissions() {return canManagePermissions;}

    public Member getSelected() {
        if (this.selected() >= 0 && this.selected() < this.members().size()) {
            return this.members().get(this.selected());
        }
        return null;
    }

    public Member getSelf() {
        for (Member member : this.members()) {
            if (Objects.equals(member.profile().getId(), Minecraft.getInstance().getUser().getProfileId())) {
                return member;
            }
        }
        return null;
    }

    public OptionalInt getId(GameProfile profile) {
        if (profile != null) {
            for (int i = 0; i < this.members().size(); i++) {
                Member member = this.members().get(i);
                if (member.profile().equals(profile)) {
                    return OptionalInt.of(i);
                }
            }
        }
        return OptionalInt.empty();
    }

    public static class Serializer implements MenuContentSerializer<MembersContent> {

        private final Factory<?> factory;
        private final CreateMemberFactory createMemberFactory;

        public Serializer(Factory<?> factory, CreateMemberFactory createMember) {
            this.factory = factory;
            this.createMemberFactory = createMember;
        }

        @Override
        public MembersContent from(FriendlyByteBuf buffer) {
            List<Member> members = buffer.readList(buf -> {
                GameProfile profile = buf.readGameProfile();
                MemberState state = buf.readEnum(MemberState.class);
                Set<String> permissions = buf.readCollection(Sets::newHashSetWithExpectedSize, FriendlyByteBuf::readUtf);
                String role = buf.readUtf();
                Member member = createMemberFactory.createMember(profile, state, permissions);
                member.setRole(role);
                return member;
            });
            boolean removeMembers = buffer.readBoolean();
            boolean canChangePermissions = buffer.readBoolean();
            UUID id = buffer.readUUID();
            int selected = buffer.readInt();
            return factory.createMemberContent(id, selected, members, removeMembers, canChangePermissions);
        }

        @Override
        public void to(FriendlyByteBuf buffer, MembersContent content) {
            buffer.writeCollection(content.members(), (buf, member) -> {
                buf.writeGameProfile(member.profile());
                buf.writeEnum(member.getState());
                buf.writeCollection(member.permissions(), FriendlyByteBuf::writeUtf);
                buf.writeUtf(member.getRole());
            });
            buffer.writeBoolean(content.canManageMembers());
            buffer.writeBoolean(content.canManagePermissions());
            buffer.writeUUID(content.id());
            buffer.writeInt(content.selected());
        }


        @FunctionalInterface
        public interface Factory<T extends MembersContent> {
            T createMemberContent(UUID id, int selected, List<? extends Member> members, boolean canManageMembers, boolean canManagePermissions);
        }

        @FunctionalInterface
        public interface CreateMemberFactory {
            Member createMember(GameProfile profile, MemberState state, Set<String> permissions);
        }
    }
}
