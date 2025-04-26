package earth.terrarium.odyssey_guilds.common.network.packets;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import earth.terrarium.odyssey_guilds.OdysseyGuilds;
import earth.terrarium.odyssey_guilds.api.teams.MemberStatus;
import earth.terrarium.odyssey_guilds.api.teams.guild.GuildApi;
import earth.terrarium.odyssey_guilds.client.OdysseyGuildsClient;

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
                OdysseyGuilds.id("modify_guild_member"),
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
            return () -> GuildApi.API.get(OdysseyGuildsClient.level(), packet.id()).ifPresent(guild ->
                GuildApi.API.modifyMember(OdysseyGuildsClient.level(), guild, packet.playerId(), packet.status()));
        }
    }
}
