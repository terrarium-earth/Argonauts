package earth.terrarium.odyssey_guilds.common.network.packets;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import earth.terrarium.odyssey_guilds.OdysseyGuilds;
import earth.terrarium.odyssey_guilds.api.teams.party.PartyApi;
import earth.terrarium.odyssey_guilds.client.OdysseyGuildsClient;

import java.util.UUID;

public record ClientboundLeavePartyPacket(
    UUID id,
    UUID playerId
) implements Packet<ClientboundLeavePartyPacket> {

    public static final ClientboundPacketType<ClientboundLeavePartyPacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundLeavePartyPacket> type() {
        return TYPE;
    }

    private static class Type extends CodecPacketType<ClientboundLeavePartyPacket> implements ClientboundPacketType<ClientboundLeavePartyPacket> {

        public Type() {
            super(
                ClientboundLeavePartyPacket.class,
                OdysseyGuilds.id("leave_party"),
                ObjectByteCodec.create(
                    ByteCodec.UUID.fieldOf(ClientboundLeavePartyPacket::id),
                    ByteCodec.UUID.fieldOf(ClientboundLeavePartyPacket::playerId),
                    ClientboundLeavePartyPacket::new
                )
            );
        }

        @Override
        public Runnable handle(ClientboundLeavePartyPacket packet) {
            return () -> PartyApi.API.get(packet.id()).ifPresent(party ->
                PartyApi.API.leave(OdysseyGuildsClient.level(), party, packet.playerId()));
        }
    }
}
