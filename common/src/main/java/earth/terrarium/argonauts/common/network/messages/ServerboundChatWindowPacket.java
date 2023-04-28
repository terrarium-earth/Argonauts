package earth.terrarium.argonauts.common.network.messages;

import com.google.common.primitives.UnsignedInteger;
import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.common.handlers.chat.ChatHandler;
import earth.terrarium.argonauts.common.handlers.chat.ChatMessage;
import earth.terrarium.argonauts.common.handlers.party.Party;
import earth.terrarium.argonauts.common.handlers.party.PartyHandler;
import earth.terrarium.argonauts.common.handlers.party.members.PartyMember;
import earth.terrarium.argonauts.common.menus.ChatMenu;
import earth.terrarium.argonauts.common.network.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

import java.time.Instant;

public record ServerboundChatWindowPacket(String message) implements Packet<ServerboundChatWindowPacket> {

    public static final ResourceLocation ID = new ResourceLocation(Argonauts.MOD_ID, "send_chat_message");
    public static final PacketHandler<ServerboundChatWindowPacket> HANDLER = new Handler();

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<ServerboundChatWindowPacket> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<ServerboundChatWindowPacket> {

        @Override
        public void encode(ServerboundChatWindowPacket message, FriendlyByteBuf buffer) {
            buffer.writeUtf(message.message, ChatMessage.MAX_MESSAGE_LENGTH);
        }

        @Override
        public ServerboundChatWindowPacket decode(FriendlyByteBuf buffer) {
            return new ServerboundChatWindowPacket(buffer.readUtf(ChatMessage.MAX_MESSAGE_LENGTH));
        }

        @Override
        public PacketContext handle(ServerboundChatWindowPacket message) {
            return (player, level) -> {
                var containerMenu = player.containerMenu;
                if (containerMenu instanceof ChatMenu menu) {
                    switch (menu.type()) {
                        case PARTY -> {
                            Party party = PartyHandler.get(player);
                            if (party != null && player.getServer() != null) {
                                ChatMessage chatMessage = new ChatMessage(
                                    player.getGameProfile(),
                                    message.message,
                                    Instant.now()
                                );
                                UnsignedInteger id = ChatHandler.getPartyChannel(party).add(chatMessage);

                                ClientboundReceiveMessagePacket packet = new ClientboundReceiveMessagePacket(id, chatMessage);

                                for (PartyMember member : party.members()) {
                                    var memberPlayer = player.getServer().getPlayerList().getPlayer(member.profile().getId());
                                    if (memberPlayer != null) {
                                        NetworkHandler.CHANNEL.sendToPlayer(packet, memberPlayer);
                                    }
                                }
                            }
                        }
                        case GUILD -> {
                            //TODO: Implement other chat types
                        }
                        default -> {
                            //TODO: Implement other chat types
                        }
                    }
                }
            };
        }
    }
}
