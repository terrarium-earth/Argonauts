package earth.terrarium.argonauts.common.commands.friends;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

import java.util.Collection;

public class RespondCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        LiteralCommandNode<CommandSourceStack> literalCommandNode = dispatcher.register(
            Commands.literal("msg")
                .then(Commands.argument("targets", EntityArgument.players()).then(Commands.argument("message", MessageArgument.message()).executes(commandContext -> {
                    Collection<ServerPlayer> collection = EntityArgument.getPlayers(commandContext, "targets");
                    if (!collection.isEmpty()) {
                        MessageArgument.resolveChatMessage(commandContext, "message", arg -> sendMessage(commandContext.getSource(), collection, arg));
                    }

                    return collection.size();
                })))
        );
    }

    private static void sendMessage(CommandSourceStack source, Collection<ServerPlayer> targets, PlayerChatMessage message) {
        ChatType.Bound bound = ChatType.bind(ChatType.MSG_COMMAND_INCOMING, source);
        OutgoingChatMessage outgoingChatMessage = OutgoingChatMessage.create(message);
        boolean bl = false;

        for (ServerPlayer serverPlayer : targets) {
            ChatType.Bound bound2 = ChatType.bind(ChatType.MSG_COMMAND_OUTGOING, source).withTargetName(serverPlayer.getDisplayName());
            source.sendChatMessage(outgoingChatMessage, false, bound2);
            boolean bl2 = source.shouldFilterMessageTo(serverPlayer);
            serverPlayer.sendChatMessage(outgoingChatMessage, bl2, bound);
            bl |= bl2 && message.isFullyFiltered();
        }

        if (bl) {
            source.sendSystemMessage(PlayerList.CHAT_FILTERED_FULL);
        }
    }
}
