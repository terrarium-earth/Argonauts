package earth.terrarium.argonauts.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import earth.terrarium.argonauts.common.commands.base.ChatCommands;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.chat.ChatMessageType;
import earth.terrarium.argonauts.common.handlers.party.Party;
import earth.terrarium.argonauts.common.handlers.party.PartyHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public final class PartyChatCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        CommandHelper.register(dispatcher, "party", "chat", PartyChatCommands::openChatScreen);
    }

    public static void openChatScreen(ServerPlayer player) throws MemberException {
        Party party = PartyHandler.get(player);
        if (party == null) {
            throw MemberException.YOU_ARE_NOT_IN_PARTY;
        }
        ChatCommands.openChatScreen(player,
            party,
            ChatMessageType.PARTY,
            ConstantComponents.PARTY_CHAT_TITLE
        );
    }
}

