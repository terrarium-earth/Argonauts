package earth.terrarium.argonauts.common.commands.base;

import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.members.Group;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import earth.terrarium.argonauts.common.handlers.chat.ChatHandler;
import earth.terrarium.argonauts.common.handlers.chat.ChatMessageType;
import earth.terrarium.argonauts.common.menus.ChatContent;
import earth.terrarium.argonauts.common.network.NetworkHandler;
import earth.terrarium.argonauts.common.network.messages.ClientboundOpenChatMenuPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

public final class ChatCommands {
    public static void openChatScreen(ServerPlayer player, Group<?> group, ChatMessageType type, Component title) throws MemberException {
        if (!NetworkHandler.CHANNEL.canSendPlayerPackets(player)) throw MemberException.NOT_INSTALLED_ON_CLIENT;

        List<String> usernames = new ArrayList<>();
        for (Member member : group.members()) {
            ServerPlayer memberPlayer = player.server.getPlayerList().getPlayer(member.profile().getId());
            if (memberPlayer != null) {
                usernames.add(memberPlayer.getGameProfile().getName());
            }
        }

        NetworkHandler.CHANNEL.sendToPlayer(new ClientboundOpenChatMenuPacket(
            new ChatContent(
                type,
                group.members().size(),
                usernames,
                ChatHandler.getChannel(group, type).messages()
            ),
            title), player);
    }
}

