package earth.terrarium.argonauts.common.network.packets;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.teams.party.PartyApi;
import earth.terrarium.argonauts.client.ArgonautsClient;
import net.minecraft.resources.ResourceLocation;

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
                Argonauts.id("leave_party"),
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
                PartyApi.API.leave(ArgonautsClient.level(), party, packet.playerId()));
        }
    }
}
