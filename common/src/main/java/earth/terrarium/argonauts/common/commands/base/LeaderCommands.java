package earth.terrarium.argonauts.common.commands.base;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.members.Group;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public final class LeaderCommands {
    public static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> disband(CommandHelper.GetGroupAction getGroupAction, RemoveAction removeAction) {
        return Commands.literal("disband")
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                Group<?> group = getGroupAction.getGroup(player, false);
                CommandHelper.runAction(() -> {
                    if (group.members().isLeader(player.getUUID())) {
                        CommandHelper.runAction(() -> removeAction.remove(group, player.server));
                    } else {
                        throw MemberException.YOU_ARE_NOT_THE_LEADER_OF_PARTY;
                    }
                });
                return 1;
            });
    }

    public static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> transfer(CommandHelper.GetGroupAction getGroupAction) {
        return Commands.literal("transfer").then(Commands.argument("player", EntityArgument.player())
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                ServerPlayer target = EntityArgument.getPlayer(context, "player");
                Group<?> group = getGroupAction.getGroup(player, false);
                CommandHelper.runAction(() -> {
                    if (group.members().isLeader(player.getUUID())) {
                        CommandHelper.runAction(() -> group.members().setLeader(target.getUUID()));
                    } else {
                        throw MemberException.YOU_ARE_NOT_THE_LEADER_OF_PARTY;
                    }
                });
                return 1;
            }));
    }

    @FunctionalInterface
    public interface RemoveAction {
        void remove(Group<?> group, MinecraftServer server) throws MemberException;
    }
}
