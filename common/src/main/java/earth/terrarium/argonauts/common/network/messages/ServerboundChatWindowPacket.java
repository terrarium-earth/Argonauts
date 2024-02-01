package earth.terrarium.argonauts.common.network.messages;

import com.google.common.primitives.UnsignedInteger;
import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.base.ServerboundPacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import com.teamresourceful.resourcefullib.common.utils.CommonUtils;
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
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.time.Instant;
import java.util.function.Consumer;

public record ServerboundChatWindowPacket(String message,
                                          ChatMessageType messageType) implements Packet<ServerboundChatWindowPacket> {

    public static final ServerboundPacketType<ServerboundChatWindowPacket> TYPE = new Type();

    @Override
    public PacketType<ServerboundChatWindowPacket> type() {
        return TYPE;
    }

    private static class Type extends CodecPacketType<ServerboundChatWindowPacket> implements ServerboundPacketType<ServerboundChatWindowPacket> {

        public Type() {
            super(
                ServerboundChatWindowPacket.class,
                new ResourceLocation(Argonauts.MOD_ID, "send_chat_message"),
                ObjectByteCodec.create(
                    ByteCodec.STRING.fieldOf(ServerboundChatWindowPacket::message),
                    ByteCodec.ofEnum(ChatMessageType.class).fieldOf(ServerboundChatWindowPacket::messageType),
                    ServerboundChatWindowPacket::new
                )
            );
        }

        @Override
        public Consumer<Player> handle(ServerboundChatWindowPacket packet) {
            return player -> {
                ServerPlayer serverPlayer = (ServerPlayer) player;
                switch (packet.messageType()) {
                    case PARTY -> {
                        Party party = PartyApi.API.get(serverPlayer);
                        if (party == null) return;
                        MessageChannel channel = ChatHandler.getChannel(party, packet.messageType());
                        sendMessage(serverPlayer, party, packet.message, channel);
                    }
                    case GUILD -> {
                        Guild guild = GuildApi.API.get(serverPlayer);
                        if (guild == null) return;
                        MessageChannel channel = ChatHandler.getChannel(guild, packet.messageType());
                        sendMessage(serverPlayer, guild, packet.message, channel);
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + packet.type());
                }
            };
        }
    }

    public static void sendMessage(ServerPlayer player, Group<?, ?> group, String message, MessageChannel channel) {
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
            if (member.getState().isPermanentMember()) {
                Component messageComponent = CommonUtils.serverTranslatable("text.argonauts.chat_message",
                    group.displayName().getString(),
                    player.getGameProfile().getName(),
                    message);
                memberPlayer.displayClientMessage(messageComponent, false);
            }

            if (!NetworkHandler.CHANNEL.canSendToPlayer(memberPlayer, packet.type())) continue;
            NetworkHandler.CHANNEL.sendToPlayer(packet, memberPlayer);
        }
    }
}
