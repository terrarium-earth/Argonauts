package earth.terrarium.argonauts.client.screens.guild.members;

import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.base.members.MemberSettingList;
import earth.terrarium.argonauts.client.screens.base.members.MembersScreen;
import earth.terrarium.argonauts.client.screens.base.members.entries.BooleanEntry;
import earth.terrarium.argonauts.client.screens.base.members.entries.DividerEntry;
import earth.terrarium.argonauts.client.utils.MouseLocationFix;
import earth.terrarium.argonauts.common.handlers.base.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import earth.terrarium.argonauts.common.menus.base.MembersMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class GuildMembersScreen extends MembersScreen {

    public GuildMembersScreen(MembersMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
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
            list.addEntry(new DividerEntry(Component.translatable("argonauts.member.cadmus_permissions")));
            list.addEntry(new BooleanEntry(MemberPermissions.TEMPORARY_GUILD_PERMISSIONS, member.hasPermission(MemberPermissions.TEMPORARY_GUILD_PERMISSIONS), !cantModify && this.menu.canManagePermissions() && self.hasPermission(MemberPermissions.TEMPORARY_GUILD_PERMISSIONS)));
        }
    }
}
