package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import earth.terrarium.argonauts.common.commands.base.BaseModCommands;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public final class GuildModCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("guild")
            .then(warp())
            .then(tp())
        );
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> tp() {
        return BaseModCommands.tp(
            GuildCommandHelper::getGuildOrThrow,
            MemberException.NOT_IN_SAME_GUILD,
            MemberException.YOU_CANT_TP_MEMBERS_IN_GUILD,
            (group, targetMember) -> {}
        );
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> warp() {
        return BaseModCommands.warp(
            GuildCommandHelper::getGuildOrThrow,
            MemberException.YOU_CANT_TP_MEMBERS_IN_GUILD);
    }
}
