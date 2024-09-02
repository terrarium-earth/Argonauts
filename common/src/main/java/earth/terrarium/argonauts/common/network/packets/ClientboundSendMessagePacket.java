package earth.terrarium.argonauts.common.network.packets;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.teams.guild.GuildApi;
import earth.terrarium.argonauts.api.teams.party.PartyApi;
import earth.terrarium.argonauts.client.ArgonautsClient;
import earth.terrarium.argonauts.client.screens.chat.ChatScreen;
import earth.terrarium.argonauts.common.chat.ChatHandler;
import earth.terrarium.argonauts.common.chat.ChatMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.UUID;

public record ClientboundSendMessagePacket(
    UUID id,
    ChatMessage message
) implements Packet<ClientboundSendMessagePacket> {

    public static final ClientboundPacketType<ClientboundSendMessagePacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundSendMessagePacket> type() {
        return TYPE;
    }

    private static class Type extends CodecPacketType<ClientboundSendMessagePacket> implements ClientboundPacketType<ClientboundSendMessagePacket> {

        public Type() {
            super(
                ClientboundSendMessagePacket.class,
                Argonauts.id("send_message"),
                ObjectByteCodec.create(
                    ByteCodec.UUID.fieldOf(ClientboundSendMessagePacket::id),
                    ChatMessage.BYTE_CODEC.fieldOf(ClientboundSendMessagePacket::message),
                    ClientboundSendMessagePacket::new
                )
            );
        }

        @Override
        public Runnable handle(ClientboundSendMessagePacket packet) {
            return () -> {
                Level level = ArgonautsClient.level();
                GuildApi.API.get(level, packet.id).ifPresent(guild -> ChatHandler.sendMessage(level, guild, packet.message));
                PartyApi.API.get(packet.id).ifPresent(party -> ChatHandler.sendMessage(level, party, packet.message));

                if (Minecraft.getInstance().screen instanceof ChatScreen screen) {
                    screen.addMessage(packet.message);
                }
            };
        }
    }
}
