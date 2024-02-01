package earth.terrarium.argonauts.common.network.messages;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.guild.Guild;
import earth.terrarium.argonauts.client.handlers.guild.GuildClientApiImpl;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;
import java.util.UUID;

public record ClientboundSyncGuildsPacket(Set<Guild> guilds,
                                          Set<UUID> removed) implements Packet<ClientboundSyncGuildsPacket> {

    public static final ClientboundPacketType<ClientboundSyncGuildsPacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundSyncGuildsPacket> type() {
        return TYPE;
    }

    private static class Type extends CodecPacketType<ClientboundSyncGuildsPacket> implements ClientboundPacketType<ClientboundSyncGuildsPacket> {

        public Type() {
            super(
                ClientboundSyncGuildsPacket.class,
                new ResourceLocation(Argonauts.MOD_ID, "sync_guilds"),
                ObjectByteCodec.create(
                    Guild.BYTE_CODEC.setOf().fieldOf(ClientboundSyncGuildsPacket::guilds),
                    ByteCodec.UUID.setOf().fieldOf(ClientboundSyncGuildsPacket::removed),
                    ClientboundSyncGuildsPacket::new
                ));
        }

        @Override
        public Runnable handle(ClientboundSyncGuildsPacket packet) {
            return () -> GuildClientApiImpl.update(packet.guilds, packet.removed);
        }
    }
}
