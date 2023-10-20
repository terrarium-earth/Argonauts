package earth.terrarium.argonauts.common.network.messages;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.networking.base.CodecPacketHandler;
import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.guild.Guild;
import earth.terrarium.argonauts.client.handlers.guild.GuildClientApiImpl;
import net.minecraft.resources.ResourceLocation;

import java.util.Set;
import java.util.UUID;

public record ClientboundSyncGuildsPacket(Set<Guild> guilds,
                                          Set<UUID> removed) implements Packet<ClientboundSyncGuildsPacket> {

    public static final ResourceLocation ID = new ResourceLocation(Argonauts.MOD_ID, "sync_guilds");
    public static final PacketHandler<ClientboundSyncGuildsPacket> HANDLER = new Handler();

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<ClientboundSyncGuildsPacket> getHandler() {
        return HANDLER;
    }

    private static class Handler extends CodecPacketHandler<ClientboundSyncGuildsPacket> {

        public Handler() {
            super(ObjectByteCodec.create(
                Guild.BYTE_CODEC.setOf().fieldOf(ClientboundSyncGuildsPacket::guilds),
                ByteCodec.UUID.setOf().fieldOf(ClientboundSyncGuildsPacket::removed),
                ClientboundSyncGuildsPacket::new
            ));
        }

        @Override
        public PacketContext handle(ClientboundSyncGuildsPacket message) {
            return (player, level) -> GuildClientApiImpl.update(message.guilds, message.removed);
        }
    }
}
