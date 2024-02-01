package earth.terrarium.argonauts.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import earth.terrarium.argonauts.api.party.Party;
import earth.terrarium.argonauts.api.party.PartyApi;
import earth.terrarium.argonauts.common.commands.base.ChatCommands;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.chat.ChatHandler;
import earth.terrarium.argonauts.common.handlers.chat.ChatMessageType;
import earth.terrarium.argonauts.common.handlers.chat.MessageChannel;
import earth.terrarium.argonauts.common.network.messages.ServerboundChatWindowPacket;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public final class PartyChatCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        CommandHelper.register(dispatcher, "party", "chat", PartyChatCommands::openChatScreen);
        dispatcher.register(Commands.literal("party").then(sendMessage()));
    }

    public static void openChatScreen(ServerPlayer player) throws MemberException {
        Party party = PartyApi.API.get(player);
        if (party == null) throw MemberException.YOU_ARE_NOT_IN_PARTY;
        ChatCommands.openChatScreen(player,
            party,
            ChatMessageType.PARTY,
            ConstantComponents.PARTY_CHAT_TITLE
        );
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> sendMessage() {
        return Commands.literal("chat").then(Commands.argument("message", StringArgumentType.greedyString())
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                CommandHelper.runAction(() -> {
                    Party party = PartyApi.API.get(player);
                    if (party == null) throw MemberException.YOU_ARE_NOT_IN_PARTY;
                    String message = StringArgumentType.getString(context, "message");
                    MessageChannel channel = ChatHandler.getChannel(party, ChatMessageType.PARTY);
                    ServerboundChatWindowPacket.sendMessage(player, party, message, channel);
                });
                return 1;
            }));
    }
}

