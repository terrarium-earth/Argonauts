package earth.terrarium.odyssey_guilds.common.network.packets;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import earth.terrarium.odyssey_guilds.OdysseyGuilds;
import earth.terrarium.odyssey_guilds.api.teams.MemberStatus;
import earth.terrarium.odyssey_guilds.api.teams.party.PartyApi;
import earth.terrarium.odyssey_guilds.client.OdysseyGuildsClient;

import java.util.UUID;

public record ClientboundModifyPartyMemberPacket(
    UUID id,
    UUID playerId,
    MemberStatus status
) implements Packet<ClientboundModifyPartyMemberPacket> {

    public static final ClientboundPacketType<ClientboundModifyPartyMemberPacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundModifyPartyMemberPacket> type() {
        return TYPE;
    }

    private static class Type extends CodecPacketType<ClientboundModifyPartyMemberPacket> implements ClientboundPacketType<ClientboundModifyPartyMemberPacket> {

        public Type() {
            super(
                ClientboundModifyPartyMemberPacket.class,
                OdysseyGuilds.id("modify_party_member"),
                ObjectByteCodec.create(
                    ByteCodec.UUID.fieldOf(ClientboundModifyPartyMemberPacket::id),
                    ByteCodec.UUID.fieldOf(ClientboundModifyPartyMemberPacket::playerId),
                    MemberStatus.BYTE_CODEC.fieldOf(ClientboundModifyPartyMemberPacket::status),
                    ClientboundModifyPartyMemberPacket::new
                )
            );
        }

        @Override
        public Runnable handle(ClientboundModifyPartyMemberPacket packet) {
            return () -> PartyApi.API.get(packet.id()).ifPresent(party ->
                PartyApi.API.modifyMember(OdysseyGuildsClient.level(), party, packet.playerId(), packet.status()));
        }
    }
}
