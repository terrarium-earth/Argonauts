package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.argonauts.api.teams.guild.Guild;
import earth.terrarium.argonauts.api.teams.guild.GuildApi;
import earth.terrarium.argonauts.common.chat.ChatHandler;
import earth.terrarium.argonauts.common.chat.ChatMessage;
import earth.terrarium.argonauts.common.commands.TeamExceptions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.time.Instant;

public final class GuildChatCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("guild")
            .then(Commands.literal("chat")
                .then(Commands.argument("message", StringArgumentType.greedyString())
                    .executes(context -> {
                        sendMessage(context.getSource(), StringArgumentType.getString(context, "message"));
                        return 1;
                    })
                )
            )
        );
        dispatcher.register(Commands.literal("gc")
            .then(Commands.argument("message", StringArgumentType.greedyString())
                .executes(context -> {
                    sendMessage(context.getSource(), StringArgumentType.getString(context, "message"));
                    return 1;
                })
            )
        );
    }

    private static void sendMessage(CommandSourceStack source, String message) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Guild guild = GuildApi.API.getPlayerGuild(player).orElse(null);
        if (guild == null) throw TeamExceptions.NOT_IN_GUILD.create();
        ChatHandler.sendMessage(source.getLevel(), guild, new ChatMessage(player.getGameProfile(), message, Instant.now()));
    }
}
