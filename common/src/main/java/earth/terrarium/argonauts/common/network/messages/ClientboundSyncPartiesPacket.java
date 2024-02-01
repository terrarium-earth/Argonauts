package earth.terrarium.argonauts.common.network.messages;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.party.Party;
import earth.terrarium.argonauts.client.handlers.party.PartyClientApiImpl;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;
import java.util.UUID;

public record ClientboundSyncPartiesPacket(Set<Party> parties,
                                           Set<UUID> removed) implements Packet<ClientboundSyncPartiesPacket> {

    public static final ClientboundPacketType<ClientboundSyncPartiesPacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundSyncPartiesPacket> type() {
        return TYPE;
    }

    private static class Type extends CodecPacketType<ClientboundSyncPartiesPacket> implements ClientboundPacketType<ClientboundSyncPartiesPacket> {

        public Type() {
            super(
                ClientboundSyncPartiesPacket.class,
                new ResourceLocation(Argonauts.MOD_ID, "sync_parties"),
                ObjectByteCodec.create(
                    Party.BYTE_CODEC.setOf().fieldOf(ClientboundSyncPartiesPacket::parties),
                    ByteCodec.UUID.setOf().fieldOf(ClientboundSyncPartiesPacket::removed),
                    ClientboundSyncPartiesPacket::new
                ));
        }

        @Override
        public Runnable handle(ClientboundSyncPartiesPacket packet) {
            return () -> PartyClientApiImpl.update(packet.parties, packet.removed);
        }
    }
}
