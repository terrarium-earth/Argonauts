package earth.terrarium.odyssey_allies.common.commands.friends;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.odyssey_allies.api.friends.FriendsApi;
import earth.terrarium.odyssey_allies.common.constants.ConstantComponents;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class FriendListCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("friends")
            .then(Commands.literal("list")
                .executes(context -> {
                    list(context.getSource());
                    return 1;
                })
            )
        );
    }

    private static void list(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        // Implement the logic to list friends for the player
        // This is a placeholder for the actual implementation
        source.sendSuccess(() -> ConstantComponents.FRIEND_LIST, true);
        var friends = FriendsApi.API.getFriends(player);
        var list = CommonComponents.EMPTY.copy();
        for (GameProfile friend : friends) {
            var friendPlayer = source.getServer().getPlayerList().getPlayer(friend.getId());
            if (friendPlayer != null) {
                list = list.append(Component.literal(friendPlayer.getName().getString()).withStyle(friendPlayer.isSpectator() ? ChatFormatting.GRAY : ChatFormatting.GREEN));
            } else {
                list = list.append(Component.literal(friend.getName()).withStyle(ChatFormatting.GRAY));
            }
            list = list.append(Component.literal(", "));
        }
        Component finalList = list;
        source.sendSuccess(() -> finalList, true);
    }
}
