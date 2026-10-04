package earth.terrarium.argonauts.common.network.packets;

import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.teams.guild.Guild;
import earth.terrarium.argonauts.api.teams.guild.GuildApi;
import earth.terrarium.argonauts.client.ArgonautsClient;

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
                Argonauts.id("sync_guilds"),
                ObjectByteCodec.create(
                    Guild.BYTE_CODEC.setOf().fieldOf(ClientboundSyncGuildsPacket::guilds),
                    ClientboundSyncGuildsPacket::new
                )
            );
        }

        @Override
        public Runnable handle(ClientboundSyncGuildsPacket packet) {
            return () -> packet.guilds.forEach(guild -> GuildApi.API.create(ArgonautsClient.level(), guild));
        }
    }
}
