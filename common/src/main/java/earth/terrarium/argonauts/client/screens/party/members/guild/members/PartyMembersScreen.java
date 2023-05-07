package earth.terrarium.argonauts.client.screens.party.members.guild.members;

import earth.terrarium.argonauts.client.screens.base.members.MembersScreen;
import earth.terrarium.argonauts.client.utils.MouseLocationFix;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import earth.terrarium.argonauts.common.menus.base.MembersMenu;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class PartyMembersScreen extends MembersScreen {

    public PartyMembersScreen(MembersMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
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
}
