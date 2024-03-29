package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import earth.terrarium.argonauts.api.guild.GuildApi;
import earth.terrarium.argonauts.common.commands.base.LeaderCommands;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public final class GuildLeaderCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("guild")
            .then(disband())
            .then(transfer())
        );
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> disband() {
        return LeaderCommands.disband(
            GuildCommandHelper::getGuildOrThrow,
            GuildApi.API::disband,
            MemberException.YOU_ARE_NOT_THE_OWNER_OF_GUILD);
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> transfer() {
        return LeaderCommands.transfer(
            GuildCommandHelper::getGuildOrThrow,
            MemberException.YOU_ARE_NOT_THE_OWNER_OF_GUILD);
    }
}
