package earth.terrarium.odyssey_guilds.common.network.packets;

import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import earth.terrarium.odyssey_guilds.OdysseyGuilds;
import earth.terrarium.odyssey_guilds.api.teams.guild.Guild;
import earth.terrarium.odyssey_guilds.api.teams.guild.GuildApi;
import earth.terrarium.odyssey_guilds.client.OdysseyGuildsClient;

import java.util.Set;

public record ClientboundSyncGuildsPacket(
    Set<Guild> guilds
) implements Packet<ClientboundSyncGuildsPacket> {

    public static final ClientboundPacketType<ClientboundSyncGuildsPacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundSyncGuildsPacket> type() {
        return TYPE;
    }

    private static class Type extends CodecPacketType<ClientboundSyncGuildsPacket> implements ClientboundPacketType<ClientboundSyncGuildsPacket> {

        public Type() {
            super(
                ClientboundSyncGuildsPacket.class,
                OdysseyGuilds.id("sync_guilds"),
                ObjectByteCodec.create(
                    Guild.BYTE_CODEC.setOf().fieldOf(ClientboundSyncGuildsPacket::guilds),
                    ClientboundSyncGuildsPacket::new
                )
            );
        }

        @Override
        public Runnable handle(ClientboundSyncGuildsPacket packet) {
            return () -> packet.guilds.forEach(guild -> GuildApi.API.create(OdysseyGuildsClient.level(), guild));
        }
    }
}
