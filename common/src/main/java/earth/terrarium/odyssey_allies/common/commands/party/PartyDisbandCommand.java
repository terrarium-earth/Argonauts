package earth.terrarium.odyssey_allies.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.odyssey_allies.api.teams.party.Party;
import earth.terrarium.odyssey_allies.api.teams.party.PartyApi;
import earth.terrarium.odyssey_allies.common.commands.AlliesExcepetions;
import earth.terrarium.odyssey_allies.common.utils.ModUtils;
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
        if (party == null) throw AlliesExcepetions.NOT_IN_PARTY.create();
        if (!party.isOwner(player.getUUID())) throw AlliesExcepetions.NOT_PARTY_OWNER.create();

        PartyApi.API.disband(source.getLevel(), party);
        source.sendSuccess(() -> ModUtils.translatableWithStyle("command.odyssey_allies.party_disband", party.displayName()), false);
    }
}
