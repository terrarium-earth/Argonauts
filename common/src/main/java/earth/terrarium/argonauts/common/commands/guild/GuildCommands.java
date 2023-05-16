package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import earth.terrarium.argonauts.common.commands.base.BaseCommands;
import earth.terrarium.argonauts.common.handlers.guild.Guild;
import earth.terrarium.argonauts.common.handlers.guild.GuildHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public final class GuildCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("guild")
            .then(join())
            .then(leave())
        );
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> leave() {
        return BaseCommands.leave(
            GuildCommandHelper::getGuildOrThrow,
            GuildHandler::leave);
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> join() {
        return BaseCommands.join(
            GuildCommandHelper::getGuildOrThrow,
            (group, player) -> GuildHandler.tryJoin((Guild) group, player));
    }
}
