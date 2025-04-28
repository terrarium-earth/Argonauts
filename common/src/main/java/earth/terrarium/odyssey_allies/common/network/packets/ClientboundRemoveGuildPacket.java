package earth.terrarium.odyssey_allies.common.network.packets;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import earth.terrarium.odyssey_allies.OdysseyAllies;
import earth.terrarium.odyssey_allies.api.teams.guild.GuildApi;
import earth.terrarium.odyssey_allies.client.OdysseyAlliesClient;
import earth.terrarium.odyssey_allies.common.chat.ChatHandler;

import java.util.UUID;

public record ClientboundRemoveGuildPacket(
    UUID id
) implements Packet<ClientboundRemoveGuildPacket> {

    public static final ClientboundPacketType<ClientboundRemoveGuildPacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundRemoveGuildPacket> type() {
        return TYPE;
    }

    private static class Type extends CodecPacketType<ClientboundRemoveGuildPacket> implements ClientboundPacketType<ClientboundRemoveGuildPacket> {

        public Type() {
            super(
                ClientboundRemoveGuildPacket.class,
                OdysseyAllies.id("remove_guild"),
                ObjectByteCodec.create(
                    ByteCodec.UUID.fieldOf(ClientboundRemoveGuildPacket::id),
                    ClientboundRemoveGuildPacket::new
                )
            );
        }

        @Override
        public Runnable handle(ClientboundRemoveGuildPacket packet) {
            return () -> {
                GuildApi.API.get(OdysseyAlliesClient.level(), packet.id()).ifPresent(guild ->
                    GuildApi.API.disband(OdysseyAlliesClient.level(), guild));
                ChatHandler.removeChannel(packet.id());
            };
        }
    }
}
