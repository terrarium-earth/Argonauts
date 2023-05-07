package earth.terrarium.argonauts.common.menus.party;

import earth.terrarium.argonauts.common.commands.party.PartyMemberCommands;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.menus.base.MembersContent;
import earth.terrarium.argonauts.common.menus.base.MembersMenu;
import earth.terrarium.argonauts.common.registries.ModMenus;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

public class PartyMembersMenu extends MembersMenu {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public PartyMembersMenu(int i, Inventory ignored, Optional<MembersContent> content) {
        super(ModMenus.PARTY, i, ignored, content);
    }

    public PartyMembersMenu(int id, MembersContent content) {
        super(ModMenus.PARTY, id, content);
    }

    @Override
    public void openScreen(ServerPlayer player, int i) throws MemberException {
        PartyMemberCommands.openMembersScreen(player, i);
    }
}
