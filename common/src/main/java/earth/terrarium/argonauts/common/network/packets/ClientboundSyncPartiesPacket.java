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

import java.util.Set;

public record ClientboundSyncPartiesPacket(
    Set<Party> parties
) implements Packet<ClientboundSyncPartiesPacket> {

    public static final ClientboundPacketType<ClientboundSyncPartiesPacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundSyncPartiesPacket> type() {
        return TYPE;
    }

    private static class Type extends CodecPacketType<ClientboundSyncPartiesPacket> implements ClientboundPacketType<ClientboundSyncPartiesPacket> {

        public Type() {
            super(
                ClientboundSyncPartiesPacket.class,
                Argonauts.id("sync_parties"),
                ObjectByteCodec.create(
                    Party.BYTE_CODEC.setOf().fieldOf(ClientboundSyncPartiesPacket::parties),
                    ClientboundSyncPartiesPacket::new
                )
            );
        }

        @Override
        public Runnable handle(ClientboundSyncPartiesPacket packet) {
            return () -> packet.parties.forEach(party -> PartyApi.API.create(ArgonautsClient.level(), party));
        }
    }
}
