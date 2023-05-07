package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import earth.terrarium.argonauts.common.commands.base.ManageCommands;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.guild.GuildHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public final class GuildManageCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("guild")
            .then(invite())
            .then(remove())
        );
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> invite() {
        return ManageCommands.invite(
            "guild",
            MemberException.YOU_CANT_MANAGE_MEMBERS_IN_GUILD,
            GuildCommandHelper::getGuildOrThrow
        );
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> remove() {
        return ManageCommands.remove(
            MemberException.YOU_CANT_REMOVE_YOURSELF_FROM_GUILD,
            MemberException.YOU_CANT_MANAGE_MEMBERS_IN_GUILD,
            GuildCommandHelper::getGuildOrThrow,
            GuildHandler::remove
        );
    }
}
