package earth.terrarium.argonauts.common.network.messages;

import com.google.common.primitives.UnsignedInteger;
import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.guild.Guild;
import earth.terrarium.argonauts.api.guild.GuildApi;
import earth.terrarium.argonauts.api.party.Party;
import earth.terrarium.argonauts.api.party.PartyApi;
import earth.terrarium.argonauts.common.handlers.base.members.Group;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import earth.terrarium.argonauts.common.handlers.chat.ChatHandler;
import earth.terrarium.argonauts.common.handlers.chat.ChatMessage;
import earth.terrarium.argonauts.common.handlers.chat.ChatMessageType;
import earth.terrarium.argonauts.common.handlers.chat.MessageChannel;
import earth.terrarium.argonauts.common.network.NetworkHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.time.Instant;

public record ServerboundChatWindowPacket(String message,
                                          ChatMessageType type) implements Packet<ServerboundChatWindowPacket> {

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
            buffer.writeEnum(message.type);
        }

        @Override
        public ServerboundChatWindowPacket decode(FriendlyByteBuf buffer) {
            return new ServerboundChatWindowPacket(
                buffer.readUtf(ChatMessage.MAX_MESSAGE_LENGTH),
                buffer.readEnum(ChatMessageType.class));
        }

        @Override
        public PacketContext handle(ServerboundChatWindowPacket message) {
            return (player, level) -> {
                ServerPlayer serverPlayer = (ServerPlayer) player;
                switch (message.type()) {
                    case PARTY -> {
                        Party party = PartyApi.API.get(serverPlayer);
                        if (party == null) return;
                        MessageChannel channel = ChatHandler.getChannel(party, message.type());
                        sendMessage(serverPlayer, party, message.message, channel);
                    }
                    case GUILD -> {
                        Guild guild = GuildApi.API.get(serverPlayer);
                        if (guild == null) return;
                        MessageChannel channel = ChatHandler.getChannel(guild, message.type());
                        sendMessage(serverPlayer, guild, message.message, channel);
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + message.type());
                }
            };
        }
    }

    private static void sendMessage(ServerPlayer player, Group<?> group, String message, MessageChannel channel) {
        ChatMessage chatMessage = new ChatMessage(
            player.getGameProfile(),
            message,
            Instant.now()
        );
        UnsignedInteger id = channel.add(chatMessage);

        ClientboundReceiveMessagePacket packet = new ClientboundReceiveMessagePacket(id, chatMessage);

        for (Member member : group.members()) {
            var memberPlayer = player.server.getPlayerList().getPlayer(member.profile().getId());
            if (memberPlayer == null) continue;
            if (!NetworkHandler.CHANNEL.canSendPlayerPackets(memberPlayer)) continue;
            NetworkHandler.CHANNEL.sendToPlayer(packet, memberPlayer);
        }
    }
}
