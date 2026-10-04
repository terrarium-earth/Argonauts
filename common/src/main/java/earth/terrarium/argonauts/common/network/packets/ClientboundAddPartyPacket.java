package earth.terrarium.argonauts.common.network.packets;

import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.teams.party.Party;
import earth.terrarium.argonauts.api.teams.party.PartyApi;
import earth.terrarium.argonauts.client.ArgonautsClient;

public record ClientboundAddPartyPacket(
    Party party
) implements Packet<ClientboundAddPartyPacket> {

    public static final ClientboundPacketType<ClientboundAddPartyPacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundAddPartyPacket> type() {
        return TYPE;
    }

    private static class Type extends CodecPacketType<ClientboundAddPartyPacket> implements ClientboundPacketType<ClientboundAddPartyPacket> {

        public Type() {
            super(
                ClientboundAddPartyPacket.class,
                Argonauts.id("add_party"),
                ObjectByteCodec.create(
                    Party.BYTE_CODEC.fieldOf(ClientboundAddPartyPacket::party),
                    ClientboundAddPartyPacket::new
                )
            );
        }

        @Override
        public Runnable handle(ClientboundAddPartyPacket packet) {
            return () -> PartyApi.API.create(ArgonautsClient.level(), packet.party);
        }
    }
}
