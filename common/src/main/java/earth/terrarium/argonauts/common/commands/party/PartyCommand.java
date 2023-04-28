package earth.terrarium.argonauts.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import earth.terrarium.argonauts.common.handlers.party.PartyHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.world.entity.player.Player;

public final class PartyCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("party")
            .then(Commands.literal("create")
                .executes(context -> {
                    Player player = context.getSource().getPlayerOrException();
                    PartyCommandHelper.runPartyAction(() -> PartyHandler.createParty(player));
                    return 1;
                })));
    }
}

