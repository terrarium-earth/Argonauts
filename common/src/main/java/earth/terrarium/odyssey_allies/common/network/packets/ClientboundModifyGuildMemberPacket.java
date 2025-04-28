package earth.terrarium.odyssey_allies.common.network.packets;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import earth.terrarium.odyssey_allies.OdysseyAllies;
import earth.terrarium.odyssey_allies.api.teams.MemberStatus;
import earth.terrarium.odyssey_allies.api.teams.guild.GuildApi;
import earth.terrarium.odyssey_allies.client.OdysseyAlliesClient;

import java.util.UUID;

public record ClientboundModifyGuildMemberPacket(
    UUID id,
    UUID playerId,
    MemberStatus status
) implements Packet<ClientboundModifyGuildMemberPacket> {

    public static final ClientboundPacketType<ClientboundModifyGuildMemberPacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundModifyGuildMemberPacket> type() {
        return TYPE;
    }

    private static class Type extends CodecPacketType<ClientboundModifyGuildMemberPacket> implements ClientboundPacketType<ClientboundModifyGuildMemberPacket> {

        public Type() {
            super(
                ClientboundModifyGuildMemberPacket.class,
                OdysseyAllies.id("modify_guild_member"),
                ObjectByteCodec.create(
                    ByteCodec.UUID.fieldOf(ClientboundModifyGuildMemberPacket::id),
                    ByteCodec.UUID.fieldOf(ClientboundModifyGuildMemberPacket::playerId),
                    MemberStatus.BYTE_CODEC.fieldOf(ClientboundModifyGuildMemberPacket::status),
                    ClientboundModifyGuildMemberPacket::new
                )
            );
        }

        @Override
        public Runnable handle(ClientboundModifyGuildMemberPacket packet) {
            return () -> GuildApi.API.get(OdysseyAlliesClient.level(), packet.id()).ifPresent(guild ->
                GuildApi.API.modifyMember(OdysseyAlliesClient.level(), guild, packet.playerId(), packet.status()));
        }
    }
}
