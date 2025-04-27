package earth.terrarium.argonauts.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.argonauts.api.teams.party.Party;
import earth.terrarium.argonauts.api.teams.party.PartyApi;
import earth.terrarium.argonauts.common.commands.TeamExceptions;
import earth.terrarium.argonauts.common.commands.TeamSuggestionProviders;
import earth.terrarium.argonauts.common.settings.Settings;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

public final class PartyJoinCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("party")
            .then(Commands.literal("join")
                .then(Commands.argument("player", EntityArgument.player())
                    .suggests(TeamSuggestionProviders.PLAYERS_IN_PARTY_SUGGESTION_PROVIDER)
                    .executes(context -> {
                        join(context.getSource(), EntityArgument.getPlayer(context, "player"));
                        return 1;
                    })
                )
            )
        );
    }

    private static void join(CommandSourceStack source, ServerPlayer targetPlayer) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Party party = PartyApi.API.getPlayerParty(targetPlayer).orElse(null);
        if (party == null) throw TeamExceptions.PLAYER_NOT_IN_PARTY.create();
        if (PartyApi.API.getPlayerParty(player).isPresent()) throw TeamExceptions.ALREADY_IN_PARTY.create();
        if (player.getUUID().equals(targetPlayer.getUUID())) throw TeamExceptions.NOT_IN_PARTY.create();
        if (!party.isPublic() && !party.isInvited(player.getUUID())) throw TeamExceptions.NOT_INVITED_TO_PARTY.create();

        PartyApi.API.join(source.getLevel(), party, player.getUUID());

        if (Settings.ANNOUNCE_JOIN.get(party)) {
            party.onlineMembers(source.getLevel())
                .stream()
                .filter(member -> !member.getUUID().equals(player.getUUID()))
                .forEach(member -> member.displayClientMessage(ModUtils.translatableWithStyle("command.argonauts.joined_party", player.getName()), false));
        }
        source.sendSuccess(() -> ModUtils.translatableWithStyle("command.argonauts.join_party", party.displayName()), false);
    }
}
