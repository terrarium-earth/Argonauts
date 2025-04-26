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
import earth.terrarium.odyssey_guilds.common.chat.ChatHandler;

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
                OdysseyGuilds.id("remove_party"),
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
                    PartyApi.API.disband(OdysseyGuildsClient.level(), party));
                ChatHandler.removeChannel(packet.id());
            };
        }
    }
}
