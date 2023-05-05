package earth.terrarium.argonauts.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.handlers.party.PartyHandler;
import net.minecraft.commands.CommandSourceStack;

public final class PartyCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        CommandHelper.register(
            dispatcher,
            "party",
            "create",
            PartyHandler::createParty);
    }
}

