package earth.terrarium.argonauts.common.commands.base;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.members.Group;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public final class CommandHelper {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, String command, String command2, CommandAction runAction) {
        dispatcher.register(Commands.literal(command)
            .then(Commands.literal(command2)
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    CommandHelper.runAction(() -> runAction.accept(player));
                    return 1;
                })));
    }

    public static void runAction(Action action) throws CommandSyntaxException {
        try {
            action.run();
        } catch (MemberException e) {
            throw new SimpleCommandExceptionType(e.message()).create();
        }
    }

    public static void runNetworkAction(Player player, Action action) {
        try {
            action.run();
        } catch (MemberException e) {
            player.sendSystemMessage(e.message().copy().withStyle(ChatFormatting.RED));
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @FunctionalInterface
    public interface Action {
        void run() throws MemberException, CommandSyntaxException;
    }

    @FunctionalInterface
    public interface CommandAction {
        void accept(ServerPlayer player) throws MemberException;
    }

    @FunctionalInterface
    public interface GetGroupAction<M extends Member, T extends Group<M, ?>> {
        T getGroup(ServerPlayer player, boolean otherPlayer) throws CommandSyntaxException;
    }
}