package earth.terrarium.argonauts.common.commands.friends;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.argonauts.api.friends.FriendsApi;
import earth.terrarium.argonauts.common.commands.ArgonautsExceptions;
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
        if (player.getUUID() == targetPlayer.getUUID()) throw ArgonautsExceptions.CANT_ADD_YOURSELF.create();
        if (FriendsApi.API.areFriends(source.getLevel(), player.getGameProfile(), targetPlayer.getGameProfile())) throw ArgonautsExceptions.ALREADY_FRIENDS.create();
        if (!FriendsApi.API.getPendingRequests(player).contains(targetPlayer.getGameProfile())) throw ArgonautsExceptions.NO_PENDING_REQUEST.create();
        String lang = accept ? "command.argonauts.accepted_friend_request" : "command.argonauts.denied_friend_request";
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
