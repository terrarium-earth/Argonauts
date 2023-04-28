package earth.terrarium.argonauts.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import earth.terrarium.argonauts.common.handlers.chat.ChatHandler;
import earth.terrarium.argonauts.common.handlers.chat.ChatMessageType;
import earth.terrarium.argonauts.common.handlers.party.Party;
import earth.terrarium.argonauts.common.handlers.party.PartyException;
import earth.terrarium.argonauts.common.handlers.party.PartyHandler;
import earth.terrarium.argonauts.common.handlers.party.members.PartyMember;
import earth.terrarium.argonauts.common.menus.*;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public final class PartyChatCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("party")
            .then(Commands.literal("chat")
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    PartyCommandHelper.runPartyAction(() -> openChatScreen(player));
                    return 1;
                })));
    }

    public static void openChatScreen(ServerPlayer player) throws PartyException {
        Party party = PartyHandler.get(player);
        if (party == null) {
            throw PartyException.YOU_ARE_NOT_IN_PARTY;
        }
        int partySize = 0;
        List<String> partyUsernames = new ArrayList<>();
        for (PartyMember member : party.members()) {
            ServerPlayer memberPlayer = player.server.getPlayerList().getPlayer(member.profile().getId());
            if (memberPlayer != null) {
                partyUsernames.add(memberPlayer.getGameProfile().getName());
            }
            partySize++;
        }

        BasicContentMenuProvider.open(
            new ChatContent(
                ChatMessageType.PARTY,
                partySize,
                partyUsernames,
                ChatHandler.getPartyChannel(party).messages()
            ),
            Component.literal("Party Chat"),
            ChatMenu::new,
            player
        );
    }
}

