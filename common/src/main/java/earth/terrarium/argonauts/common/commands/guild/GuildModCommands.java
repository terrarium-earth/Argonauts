package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.argonauts.common.commands.base.BaseModCommands;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import earth.terrarium.argonauts.common.handlers.guild.Guild;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerPlayer;

public final class GuildModCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("guild")
            .then(warp())
            .then(tp())

            .then(Commands.literal("hq")
                .executes(GuildModCommands::hq))
            .then(Commands.literal("headquarters")
                .executes(GuildModCommands::hq))
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

    public static int hq(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        Guild guild = GuildCommandHelper.getGuildOrThrow(player, false);
        CommandHelper.runAction(() -> {
            Member member = guild.getMember(player);
            if (member.hasPermission(MemberPermissions.TP_MEMBERS)) {
                GlobalPos hq = guild.settings().hq();
                if (hq == null) return;
                player.teleportTo(
                    player.server.getLevel(hq.dimension()),
                    hq.pos().getX(), hq.pos().getY(), hq.pos().getZ(),
                    player.getYRot(), player.getXRot()
                );
            } else {
                throw MemberException.YOU_CANT_TP_MEMBERS_IN_GUILD;
            }
        });
        return 1;
    }
}
