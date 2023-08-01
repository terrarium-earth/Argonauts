package earth.terrarium.argonauts.client.screens.party.members;

import com.teamresourceful.resourcefullib.client.utils.MouseLocationFix;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.base.members.MemberSettingList;
import earth.terrarium.argonauts.client.screens.base.members.MembersScreen;
import earth.terrarium.argonauts.client.screens.base.members.entries.BooleanEntry;
import earth.terrarium.argonauts.client.screens.base.members.entries.DividerEntry;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import earth.terrarium.argonauts.common.handlers.GroupType;
import earth.terrarium.argonauts.common.handlers.base.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import earth.terrarium.argonauts.common.menus.base.MembersContent;
import earth.terrarium.argonauts.common.menus.party.PartyMembersContent;
import earth.terrarium.argonauts.common.network.NetworkHandler;
import earth.terrarium.argonauts.common.network.messages.ServerboundRequestShowCadmusPermissionsPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.Collection;

public class PartyMembersScreen extends MembersScreen {
    private boolean showCadmusScreen;

    public PartyMembersScreen(MembersContent menuContent, Component displayName) {
        super(menuContent, displayName);
        NetworkHandler.CHANNEL.sendToServer(new ServerboundRequestShowCadmusPermissionsPacket());
    }

    @Override
    public String runRemoveCommand(Member member) {
        return "party remove " + member.profile().getName();
    }

    @Override
    public void removed() {
        super.removed();
        MouseLocationFix.setFix(clas -> clas == PartyMembersScreen.class);
    }

    @Override
    public void additionalEntries(MemberSettingList list, Member member, boolean cantModify, Member self) {
        if (!showCadmusScreen) return;
        if (!Argonauts.isCadmusLoaded()) return;

        list.addEntry(new DividerEntry(Component.translatable("argonauts.member.cadmus_permissions")));
        for (String permission : MemberPermissions.CADMUS_PERMISSIONS) {
            list.addEntry(new BooleanEntry(permission, member.hasPermission(permission), !cantModify && this.menuContent.canManagePermissions() && self.hasPermission(permission), this::groupType, () -> this.menuContent.getSelected().profile().getId()));
        }
    }

    @Override
    public Collection<String> getAdditionalPermissions() {
        return MemberPermissions.CADMUS_PERMISSIONS;
    }

    public void showCadmusPermissions() {
        showCadmusScreen = true;
        this.repositionElements();
    }

    @Override
    public GroupType groupType() {
        return GroupType.PARTY;
    }

    @Override
    public void openScreen(int selected) {
        open(
            new PartyMembersContent(
                this.menuContent.id(),
                selected,
                this.menuContent.members(),
                this.menuContent.canManageMembers(),
                this.menuContent.canManagePermissions()
            ),
            ConstantComponents.PARTY_MEMBERS_TITLE
        );
    }

    public static void open(MembersContent menuContent, Component displayName) {
        Minecraft.getInstance().setScreen(new PartyMembersScreen(menuContent, displayName));
    }
}
