package earth.terrarium.argonauts.client.screens.party.members.guild.members;

import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.base.members.MemberSettingList;
import earth.terrarium.argonauts.client.screens.base.members.MembersScreen;
import earth.terrarium.argonauts.client.screens.base.members.entries.BooleanEntry;
import earth.terrarium.argonauts.client.screens.base.members.entries.DividerEntry;
import earth.terrarium.argonauts.client.utils.MouseLocationFix;
import earth.terrarium.argonauts.common.handlers.base.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import earth.terrarium.argonauts.common.menus.base.MembersMenu;
import earth.terrarium.argonauts.common.network.NetworkHandler;
import earth.terrarium.argonauts.common.network.messages.ServerboundRequestShowCadmusPermissionsPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class PartyMembersScreen extends MembersScreen {
    public boolean showCadmusScreen;

    public PartyMembersScreen(MembersMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
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
        if (showCadmusScreen) {
            if (Argonauts.isCadmusLoaded()) {
                list.addEntry(new DividerEntry(Component.translatable("argonauts.member.cadmus_permissions")));
                for (String permission : MemberPermissions.CADMUS_PERMISSIONS) {
                    list.addEntry(new BooleanEntry(permission, member.hasPermission(permission), !cantModify && this.menu.canManagePermissions() && self.hasPermission(permission)));
                }
            }
        }
    }

    public void refreshPermissions() {
        showCadmusScreen = true;
        this.repositionElements();
    }
}
