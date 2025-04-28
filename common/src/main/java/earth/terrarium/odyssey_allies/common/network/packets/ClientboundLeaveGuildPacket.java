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

import java.util.UUID;

public record ClientboundLeaveGuildPacket(
    UUID id,
    UUID playerId
) implements Packet<ClientboundLeaveGuildPacket> {

    public static final ClientboundPacketType<ClientboundLeaveGuildPacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundLeaveGuildPacket> type() {
        return TYPE;
    }

    private static class Type extends CodecPacketType<ClientboundLeaveGuildPacket> implements ClientboundPacketType<ClientboundLeaveGuildPacket> {

        public Type() {
            super(
                ClientboundLeaveGuildPacket.class,
                OdysseyAllies.id("leave_guild"),
                ObjectByteCodec.create(
                    ByteCodec.UUID.fieldOf(ClientboundLeaveGuildPacket::id),
                    ByteCodec.UUID.fieldOf(ClientboundLeaveGuildPacket::playerId),
                    ClientboundLeaveGuildPacket::new
                )
            );
        }

        @Override
        public Runnable handle(ClientboundLeaveGuildPacket packet) {
            return () -> GuildApi.API.get(OdysseyAlliesClient.level(), packet.id()).ifPresent(guild ->
                GuildApi.API.leave(OdysseyAlliesClient.level(), guild, packet.playerId()));
        }
    }
}
