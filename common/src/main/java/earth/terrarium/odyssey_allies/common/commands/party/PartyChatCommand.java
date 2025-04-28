package earth.terrarium.odyssey_allies.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.odyssey_allies.api.teams.party.Party;
import earth.terrarium.odyssey_allies.api.teams.party.PartyApi;
import earth.terrarium.odyssey_allies.common.chat.ChatHandler;
import earth.terrarium.odyssey_allies.common.chat.ChatMessage;
import earth.terrarium.odyssey_allies.common.commands.AlliesExcepetions;
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
        if (party == null) throw AlliesExcepetions.NOT_IN_PARTY.create();
        ChatHandler.sendMessage(source.getLevel(), party, new ChatMessage(player.getGameProfile(), message, Instant.now()));
    }
}
