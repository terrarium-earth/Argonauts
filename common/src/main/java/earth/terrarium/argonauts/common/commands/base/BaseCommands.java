package earth.terrarium.argonauts.common.commands.base;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.members.Group;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public final class BaseCommands {
    public static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> leave(CommandHelper.GetGroupAction getGroupAction, RemoveAction removeAction) {
        return Commands.literal("leave")
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                Group<?> group = getGroupAction.getGroup(player, true);
                CommandHelper.runAction(() -> removeAction.remove(group.id(), player));
                return 1;
            });
    }

    public static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> join(CommandHelper.GetGroupAction getGroupAction, JoinAction joinAction) {
        return Commands.literal("join").then(Commands.argument("player", EntityArgument.player())
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                ServerPlayer target = EntityArgument.getPlayer(context, "player");
                Group<?> group = getGroupAction.getGroup(target, true);
                CommandHelper.runAction(() -> joinAction.join(group, player));
                return 1;
            }));
    }

    @FunctionalInterface
    public interface RemoveAction {
        void remove(UUID id, ServerPlayer player) throws MemberException;
    }

    @FunctionalInterface
    public interface JoinAction {
        void join(Group<?> group, ServerPlayer player) throws MemberException;
    }
}
