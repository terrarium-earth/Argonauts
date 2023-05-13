package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.guild.Guild;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerPlayer;

public final class GuildHqCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("guild")
            .then(Commands.literal("hq")
                .executes(GuildHqCommands::hq))
            .then(Commands.literal("headquarters")
                .executes(GuildHqCommands::hq))
        );
    }

    public static int hq(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer player = context.getSource().getPlayerOrException();
        Guild guild = GuildCommandHelper.getGuildOrThrow(player, false);
        CommandHelper.runAction(() -> {
            GlobalPos hq = guild.settings().hq().orElse(null);
            if (hq == null) {
                throw MemberException.HQ_NOT_SET;
            }
            player.teleportTo(
                player.server.getLevel(hq.dimension()),
                hq.pos().getX(), hq.pos().getY(), hq.pos().getZ(),
                player.getYRot(), player.getXRot()
            );
        });
        return 1;
    }
}
