package earth.terrarium.argonauts.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import earth.terrarium.argonauts.common.handlers.party.Party;
import earth.terrarium.argonauts.common.handlers.party.PartyException;
import earth.terrarium.argonauts.common.handlers.party.PartyHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.player.Player;

public final class PartyLeaderCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("party")
            .then(disband())
            .then(transfer())
        );
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> disband() {
        return Commands.literal("disband")
            .executes(context -> {
                Player player = context.getSource().getPlayerOrException();
                Party party = PartyCommandHelper.getPartyOrThrow(player, false);
                PartyCommandHelper.runPartyAction(() -> {
                    if (party.members().isLeader(player.getUUID())) {
                        PartyCommandHelper.runPartyAction(() -> PartyHandler.remove(party));
                    } else {
                        throw PartyException.YOU_ARE_NOT_THE_LEADER;
                    }
                });
                return 1;
            });
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> transfer() {
        return Commands.literal("transfer").then(Commands.argument("player", EntityArgument.player())
            .executes(context -> {
                Player player = context.getSource().getPlayerOrException();
                Player target = EntityArgument.getPlayer(context, "player");
                Party party = PartyCommandHelper.getPartyOrThrow(player, false);
                PartyCommandHelper.runPartyAction(() -> {
                    if (party.members().isLeader(player.getUUID())) {
                        PartyCommandHelper.runPartyAction(() -> party.members().setLeader(target.getUUID()));
                    } else {
                        throw PartyException.YOU_ARE_NOT_THE_LEADER;
                    }
                });
                return 1;
            }));
    }
}
