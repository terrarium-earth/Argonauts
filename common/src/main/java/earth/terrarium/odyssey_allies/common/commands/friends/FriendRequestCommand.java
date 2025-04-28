package earth.terrarium.odyssey_allies.common.commands.friends;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.odyssey_allies.api.friends.FriendsApi;
import earth.terrarium.odyssey_allies.common.commands.AlliesExcepetions;
import earth.terrarium.odyssey_allies.common.constants.ConstantComponents;
import earth.terrarium.odyssey_allies.common.utils.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

public class FriendRequestCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("friends")
            .then(Commands.literal("add")
                .then(Commands.argument("player", EntityArgument.player())
                    .executes(context -> {
                        request(context.getSource(), EntityArgument.getPlayer(context, "player"));
                        return 1;
                    })
                )
            )
        );
    }

    private static void request(CommandSourceStack source, ServerPlayer targetPlayer) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        // Implement the logic to send a friend request to the target player
        // This is a placeholder for the actual implementation
        if(FriendsApi.API.areFriends(source.getLevel(), player.getGameProfile(), targetPlayer.getGameProfile())) throw AlliesExcepetions.ALREADY_FRIENDS.create();
        if(FriendsApi.API.getPendingRequests(player).contains(targetPlayer.getGameProfile())) throw AlliesExcepetions.ALREADY_SENT_FRIEND_REQUEST.create();
        if(FriendsApi.API.getPendingRequests(targetPlayer).contains(player.getGameProfile())) {
            FriendsApi.API.acceptFriendRequest(targetPlayer, player.getGameProfile());
            source.sendSuccess(() -> Component.translatable("command.odyssey_allies.accepted_friend_request", playerName(targetPlayer)), true);
            targetPlayer.sendSystemMessage(Component.translatable("command.odyssey_allies.accepted_friend_request", playerName(player)));
        } else {
            FriendsApi.API.sendFriendRequest(source.getLevel(), player.getGameProfile(), targetPlayer.getGameProfile());
            targetPlayer.sendSystemMessage(Component.translatable("command.odyssey_allies.received_friend_request", playerName(player)));
            targetPlayer.displayClientMessage(ConstantComponents.CLICK_TO_ACCEPT.copy().withStyle(Style.EMPTY
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ModUtils.translatableWithStyle("command.odyssey_allies.accept_friend_request", playerName(player))))
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/friends accept " + player.getGameProfile().getName()))), false);
            source.sendSuccess(() -> ConstantComponents.FRIEND_REQUEST_SENT, true);
        }
    }

    public static Component playerName(ServerPlayer player) {
        return player.getDisplayName().copy().withStyle(ChatFormatting.AQUA);
    }
}
