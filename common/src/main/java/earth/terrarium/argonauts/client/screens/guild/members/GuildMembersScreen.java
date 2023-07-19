package earth.terrarium.argonauts.client.screens.guild.members;

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
import earth.terrarium.argonauts.common.menus.guild.GuildMembersContent;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class GuildMembersScreen extends MembersScreen {

    public GuildMembersScreen(MembersContent menuContent, Component displayName) {
        super(menuContent, displayName);
    }

    @Override
    public String runRemoveCommand(Member member) {
        return "guild remove " + member.profile().getName();
    }

    @Override
    public void removed() {
        super.removed();
        MouseLocationFix.setFix(clas -> clas == GuildMembersScreen.class);
    }

    @Override
    public void additionalEntries(MemberSettingList list, Member member, boolean cantModify, Member self) {
        if (Argonauts.isCadmusLoaded()) {
            list.addEntry(new DividerEntry(ConstantComponents.CADMUS_PERMISSIONS));
            list.addEntry(new BooleanEntry(MemberPermissions.TEMPORARY_GUILD_PERMISSIONS, member.hasPermission(MemberPermissions.TEMPORARY_GUILD_PERMISSIONS), !cantModify && this.menuContent.canManagePermissions() && self.hasPermission(MemberPermissions.TEMPORARY_GUILD_PERMISSIONS), this::groupType, () -> this.menuContent.getSelected().profile().getId()));
        }
    }

    @Override
    public GroupType groupType() {
        return GroupType.GUILD;
    }

    @Override
    public void openScreen(int selected) {
        open(
            new GuildMembersContent(
                this.menuContent.id(),
                selected,
                this.menuContent.members(),
                this.menuContent.canManageMembers(),
                this.menuContent.canManagePermissions()
            ),
            ConstantComponents.GUILD_MEMBERS_TITLE
        );
    }

    public static void open(MembersContent menuContent, Component displayName) {
        Minecraft.getInstance().setScreen(new GuildMembersScreen(menuContent, displayName));
    }
}
