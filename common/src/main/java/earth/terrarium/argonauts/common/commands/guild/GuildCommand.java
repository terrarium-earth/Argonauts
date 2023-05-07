package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.handlers.guild.GuildHandler;
import net.minecraft.commands.CommandSourceStack;

public final class GuildCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        CommandHelper.register(
            dispatcher,
            "guild",
            "create",
            GuildHandler::createGuild);
    }
}

