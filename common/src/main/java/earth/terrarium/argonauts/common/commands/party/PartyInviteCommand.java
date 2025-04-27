package earth.terrarium.argonauts.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.argonauts.api.teams.MemberStatus;
import earth.terrarium.argonauts.api.teams.party.Party;
import earth.terrarium.argonauts.api.teams.party.PartyApi;
import earth.terrarium.argonauts.common.commands.TeamExceptions;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

public final class PartyInviteCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("party")
            .then(Commands.literal("invite")
                .then(Commands.argument("player", EntityArgument.player())
                    .executes(context -> {
                        invite(context.getSource(), EntityArgument.getPlayer(context, "player"));
                        return 1;
                    })
                )
            )
        );
    }

    private static void invite(CommandSourceStack source, ServerPlayer targetPlayer) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Party party = PartyApi.API.getPlayerParty(player).orElse(null);
        if (party == null) throw TeamExceptions.NOT_IN_PARTY.create();
        if (player.getUUID().equals(targetPlayer.getUUID())) throw TeamExceptions.CANT_INVITE_YOURSELF.create();
        if (!party.isPublic() && !party.canManageMembers(player.getUUID())) throw TeamExceptions.NO_PERMISSION_MANAGE_MEMBERS.create();
        if (party.isMember(targetPlayer.getUUID())) throw TeamExceptions.PLAYER_IS_PARTY_MEMBER.create();
        if (party.realMembersCount() >= PartyApi.API.getMaxPartyMembers(source.getLevel(), player.getUUID())) throw TeamExceptions.PARTY_FULL.create();

        PartyApi.API.modifyMember(source.getLevel(), party, targetPlayer.getUUID(), MemberStatus.INVITED);

        source.sendSuccess(() -> ModUtils.translatableWithStyle("command.argonauts.invite", targetPlayer.getName()), false);
        targetPlayer.displayClientMessage(ModUtils.translatableWithStyle("command.argonauts.party_invited", player.getName(), party.displayName()), false);
        targetPlayer.displayClientMessage(ConstantComponents.CLICK_TO_ACCEPT.copy().withStyle(Style.EMPTY
            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ModUtils.translatableWithStyle("command.argonauts.join", party.displayName())))
            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party join " + player.getGameProfile().getName()))), false);
    }
}
