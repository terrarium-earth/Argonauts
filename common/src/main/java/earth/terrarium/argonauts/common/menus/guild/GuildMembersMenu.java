package earth.terrarium.argonauts.common.menus.guild;

import earth.terrarium.argonauts.common.commands.guild.GuildMemberCommands;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.menus.base.MembersContent;
import earth.terrarium.argonauts.common.menus.base.MembersMenu;
import earth.terrarium.argonauts.common.registries.ModMenus;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

public class GuildMembersMenu extends MembersMenu {
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")

    public GuildMembersMenu(int i, Inventory ignored, Optional<MembersContent> content) {
        super(ModMenus.GUILD, i, ignored, content);
    }

    public GuildMembersMenu(int id, MembersContent content) {
        super(ModMenus.GUILD, id, content);
    }

    @Override
    public void openScreen(ServerPlayer player, int i) throws MemberException {
        GuildMemberCommands.openMembersScreen(player, i);
    }
}
