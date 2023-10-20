package earth.terrarium.argonauts.common.network.messages;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.networking.base.CodecPacketHandler;
import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.party.Party;
import earth.terrarium.argonauts.client.handlers.party.PartyClientApiImpl;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;
import java.util.UUID;

public record ClientboundSyncPartiesPacket(Set<Party> parties,
                                           Set<UUID> removed) implements Packet<ClientboundSyncPartiesPacket> {

    public static final ResourceLocation ID = new ResourceLocation(Argonauts.MOD_ID, "sync_parties");
    public static final PacketHandler<ClientboundSyncPartiesPacket> HANDLER = new Handler();

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<ClientboundSyncPartiesPacket> getHandler() {
        return HANDLER;
    }

    private static class Handler extends CodecPacketHandler<ClientboundSyncPartiesPacket> {

        public Handler() {
            super(ObjectByteCodec.create(
                Party.BYTE_CODEC.setOf().fieldOf(ClientboundSyncPartiesPacket::parties),
                ByteCodec.UUID.setOf().fieldOf(ClientboundSyncPartiesPacket::removed),
                ClientboundSyncPartiesPacket::new
            ));
        }

        @Override
        public PacketContext handle(ClientboundSyncPartiesPacket message) {
            return (player, level) -> PartyClientApiImpl.update(message.parties, message.removed);
        }
    }
}
