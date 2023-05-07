package earth.terrarium.argonauts.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import earth.terrarium.argonauts.common.commands.base.ManageCommands;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.party.PartyHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public final class PartyManageCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("party")
            .then(invite())
            .then(remove())
        );
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> invite() {
        return ManageCommands.invite(
            "party",
            MemberException.YOU_CANT_MANAGE_MEMBERS_IN_PARTY,
            PartyCommandHelper::getPartyOrThrow
        );
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> remove() {
        return ManageCommands.remove(
            MemberException.YOU_CANT_REMOVE_YOURSELF_FROM_PARTY,
            MemberException.YOU_CANT_MANAGE_MEMBERS_IN_PARTY,
            PartyCommandHelper::getPartyOrThrow,
            PartyHandler::remove
        );
    }
}
