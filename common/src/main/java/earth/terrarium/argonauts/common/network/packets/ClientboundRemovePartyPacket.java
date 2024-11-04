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
import earth.terrarium.argonauts.common.chat.ChatHandler;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record ClientboundRemovePartyPacket(
    UUID id
) implements Packet<ClientboundRemovePartyPacket> {

    public static final ClientboundPacketType<ClientboundRemovePartyPacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundRemovePartyPacket> type() {
        return TYPE;
    }

    private static class Type extends CodecPacketType<ClientboundRemovePartyPacket> implements ClientboundPacketType<ClientboundRemovePartyPacket> {

        public Type() {
            super(
                ClientboundRemovePartyPacket.class,
                Argonauts.id("remove_party"),
                ObjectByteCodec.create(
                    ByteCodec.UUID.fieldOf(ClientboundRemovePartyPacket::id),
                    ClientboundRemovePartyPacket::new
                )
            );
        }

        @Override
        public Runnable handle(ClientboundRemovePartyPacket packet) {
            return () -> {
                PartyApi.API.get(packet.id()).ifPresent(party ->
                    PartyApi.API.disband(ArgonautsClient.level(), party));
                ChatHandler.removeChannel(packet.id());
            };
        }
    }
}
