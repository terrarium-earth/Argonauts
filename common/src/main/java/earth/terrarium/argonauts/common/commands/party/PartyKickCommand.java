package earth.terrarium.argonauts.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.argonauts.api.teams.party.Party;
import earth.terrarium.argonauts.api.teams.party.PartyApi;
import earth.terrarium.argonauts.common.commands.TeamExceptions;
import earth.terrarium.argonauts.common.commands.TeamSuggestionProviders;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

public final class PartyKickCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("party")
            .then(Commands.literal("kick")
                .then(Commands.argument("player", EntityArgument.player())
                    .suggests(TeamSuggestionProviders.CURRENT_PARTY_MEMBERS_SUGGESTION_PROVIDER)
                    .executes(context -> {
                        kick(context.getSource(), EntityArgument.getPlayer(context, "player"));
                        return 1;
                    })
                )
            )
        );
    }

    private static void kick(CommandSourceStack source, ServerPlayer targetPlayer) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Party party = PartyApi.API.getPlayerParty(player).orElse(null);
        if (party == null) throw TeamExceptions.NOT_IN_PARTY.create();
        if (!party.canManageMembers(player.getUUID())) throw TeamExceptions.NO_PERMISSION_MANAGE_MEMBERS.create();
        if (!party.isMember(targetPlayer.getUUID())) throw TeamExceptions.PLAYER_NOT_IN_PARTY.create();
        if (player.getUUID().equals(targetPlayer.getUUID())) throw TeamExceptions.CANT_KICK_YOURSELF.create();

        PartyApi.API.leave(source.getLevel(), party, targetPlayer.getUUID());

        source.sendSuccess(() -> ModUtils.translatableWithStyle("command.argonauts.kick", targetPlayer.getName()), false);
        targetPlayer.displayClientMessage(ModUtils.translatableWithStyle("command.argonauts.party_kicked", player.getName(), party.displayName()), false);
    }
}
