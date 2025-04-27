package earth.terrarium.argonauts.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.argonauts.api.teams.party.Party;
import earth.terrarium.argonauts.api.teams.party.PartyApi;
import earth.terrarium.argonauts.common.chat.ChatHandler;
import earth.terrarium.argonauts.common.chat.ChatMessage;
import earth.terrarium.argonauts.common.commands.TeamExceptions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.time.Instant;

public final class PartyChatCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("party")
            .then(Commands.literal("chat")
                .then(Commands.argument("message", StringArgumentType.greedyString())
                    .executes(context -> {
                        sendMessage(context.getSource(), StringArgumentType.getString(context, "message"));
                        return 1;
                    })
                )
            )
        );
    }

    private static void sendMessage(CommandSourceStack source, String message) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Party party = PartyApi.API.getPlayerParty(player).orElse(null);
        if (party == null) throw TeamExceptions.NOT_IN_PARTY.create();
        ChatHandler.sendMessage(source.getLevel(), party, new ChatMessage(player.getGameProfile(), message, Instant.now()));
    }
}
