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

public final class PartyDisbandCommand {


    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("party")
            .then(Commands.literal("disband")
                .executes(context -> {
                    disband(context.getSource());
                    return 1;
                })
            )
        );
    }

    private static void disband(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Party party = PartyApi.API.getPlayerParty(player).orElse(null);
        if (party == null) throw TeamExceptions.NOT_IN_PARTY.create();
        if (!party.isOwner(player.getUUID())) throw TeamExceptions.NOT_PARTY_OWNER.create();

        PartyApi.API.disband(source.getLevel(), party);
        source.sendSuccess(() -> ModUtils.translatableWithStyle("command.argonauts.party_disband", party.displayName()), false);
    }
}
