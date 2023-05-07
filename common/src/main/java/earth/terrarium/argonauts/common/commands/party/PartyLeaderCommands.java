package earth.terrarium.argonauts.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import earth.terrarium.argonauts.common.commands.base.LeaderCommands;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.party.PartyHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public final class PartyLeaderCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("party")
            .then(disband())
            .then(transfer())
        );
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> disband() {
        return LeaderCommands.disband(
            PartyCommandHelper::getPartyOrThrow,
            PartyHandler::remove,
            MemberException.YOU_ARE_NOT_THE_LEADER_OF_PARTY);
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> transfer() {
        return LeaderCommands.transfer(
            PartyCommandHelper::getPartyOrThrow,
            MemberException.YOU_ARE_NOT_THE_LEADER_OF_PARTY);
    }
}
