package earth.terrarium.argonauts.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.argonauts.api.teams.party.Party;
import earth.terrarium.argonauts.api.teams.party.PartyApi;
import earth.terrarium.argonauts.common.commands.TeamExceptions;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.GameProfileCache;

public final class PartyMemberCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("party")
            .then(Commands.literal("members")
                .then(Commands.literal("list")
                    .executes(context -> {
                        list(context.getSource());
                        return 1;
                    })
                )
                .executes(context -> {
                    list(context.getSource());
                    return 1;
                })
            )
        );
    }

    private static void list(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Party party = PartyApi.API.getPlayerParty(player).orElse(null);
        if (party == null) throw TeamExceptions.NOT_IN_PARTY.create();

        GameProfileCache profileCache = source.getServer().getProfileCache();
        if (profileCache == null) return;

        party.members().forEach((id, member) ->
            profileCache.get(id).ifPresent(profile -> source.sendSuccess(() ->
                ModUtils.translatableWithStyle("command.argonauts.list_member", profile.getName(), member.status().getDisplayName()), false)));
    }
}
