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

public record ClientboundModifyPartyPermissionPacket(
    UUID id,
    UUID playerId,
    String permission,
    boolean value
) implements Packet<ClientboundModifyPartyPermissionPacket> {

    public static final ClientboundPacketType<ClientboundModifyPartyPermissionPacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundModifyPartyPermissionPacket> type() {
        return TYPE;
    }

    private static class Type extends CodecPacketType<ClientboundModifyPartyPermissionPacket> implements ClientboundPacketType<ClientboundModifyPartyPermissionPacket> {

        public Type() {
            super(
                ClientboundModifyPartyPermissionPacket.class,
                OdysseyGuilds.id("modify_party_permission"),
                ObjectByteCodec.create(
                    ByteCodec.UUID.fieldOf(ClientboundModifyPartyPermissionPacket::id),
                    ByteCodec.UUID.fieldOf(ClientboundModifyPartyPermissionPacket::playerId),
                    ByteCodec.STRING.fieldOf(ClientboundModifyPartyPermissionPacket::permission),
                    ByteCodec.BOOLEAN.fieldOf(ClientboundModifyPartyPermissionPacket::value),
                    ClientboundModifyPartyPermissionPacket::new
                )
            );
        }

        @Override
        public Runnable handle(ClientboundModifyPartyPermissionPacket packet) {
            return () -> PartyApi.API.get(packet.id()).ifPresent(party ->
                PartyApi.API.modifyPermission(OdysseyGuildsClient.level(), party, packet.playerId(), packet.permission(), packet.value()));
        }
    }
}
