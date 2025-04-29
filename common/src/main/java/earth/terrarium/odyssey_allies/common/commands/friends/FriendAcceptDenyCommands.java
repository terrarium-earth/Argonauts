package earth.terrarium.odyssey_allies.common.commands.friends;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.odyssey_allies.api.friends.FriendsApi;
import earth.terrarium.odyssey_allies.common.commands.AlliesExcepetions;
import earth.terrarium.odyssey_allies.common.constants.ConstantComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class FriendAcceptDenyCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("friends")
            .then(Commands.literal("accept")
                .then(Commands.argument("player", EntityArgument.player())
                    .executes(context -> {
                        add(context.getSource(), EntityArgument.getPlayer(context, "player"), true);
                        return 1;
                    })
                )
            )
            .then(Commands.literal("deny")
                .then(Commands.argument("player", EntityArgument.player())
                    .executes(context -> {
                        add(context.getSource(), EntityArgument.getPlayer(context, "player"), false);
                        return 1;
                    })
                )
            )
        );
    }

    private static void add(CommandSourceStack source, ServerPlayer targetPlayer, boolean accept) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        if (player.getUUID() == targetPlayer.getUUID()) throw AlliesExcepetions.CANT_ADD_YOURSELF.create();
        if (FriendsApi.API.areFriends(source.getLevel(), player.getGameProfile(), targetPlayer.getGameProfile())) throw AlliesExcepetions.ALREADY_FRIENDS.create();
        if (!FriendsApi.API.getPendingRequests(player).contains(targetPlayer.getGameProfile())) throw AlliesExcepetions.NO_PENDING_REQUEST.create();
        String lang = accept ? "command.odyssey_allies.accepted_friend_request" : "command.odyssey_allies.denied_friend_request";
        if (accept) {
            FriendsApi.API.acceptFriendRequest(player, targetPlayer.getGameProfile());
        } else {
            FriendsApi.API.denyFriendRequest(player, targetPlayer.getGameProfile());
        }
        source.sendSuccess(() -> Component.translatable(lang, playerName(targetPlayer)), true);
        targetPlayer.sendSystemMessage(Component.translatable(lang, playerName(player)));
    }

    public static Component playerName(ServerPlayer player) {
        return player.getDisplayName().copy().withStyle(ChatFormatting.AQUA);
    }
}
