package earth.terrarium.odyssey_allies.common.network.packets;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import earth.terrarium.odyssey_allies.OdysseyAllies;
import earth.terrarium.odyssey_allies.api.teams.party.PartyApi;
import earth.terrarium.odyssey_allies.client.OdysseyAlliesClient;

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
                OdysseyAllies.id("leave_party"),
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
                PartyApi.API.leave(OdysseyAlliesClient.level(), party, packet.playerId()));
        }
    }
}
