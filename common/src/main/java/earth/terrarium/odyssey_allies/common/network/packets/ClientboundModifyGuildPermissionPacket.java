package earth.terrarium.odyssey_allies.common.network.packets;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import earth.terrarium.odyssey_allies.OdysseyAllies;
import earth.terrarium.odyssey_allies.api.teams.guild.GuildApi;
import earth.terrarium.odyssey_allies.client.OdysseyAlliesClient;

import java.util.UUID;

public record ClientboundModifyGuildPermissionPacket(
    UUID id,
    UUID playerId,
    String permission,
    boolean value
) implements Packet<ClientboundModifyGuildPermissionPacket> {

    public static final ClientboundPacketType<ClientboundModifyGuildPermissionPacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundModifyGuildPermissionPacket> type() {
        return TYPE;
    }

    private static class Type extends CodecPacketType<ClientboundModifyGuildPermissionPacket> implements ClientboundPacketType<ClientboundModifyGuildPermissionPacket> {

        public Type() {
            super(
                ClientboundModifyGuildPermissionPacket.class,
                OdysseyAllies.id("modify_guild_permission"),
                ObjectByteCodec.create(
                    ByteCodec.UUID.fieldOf(ClientboundModifyGuildPermissionPacket::id),
                    ByteCodec.UUID.fieldOf(ClientboundModifyGuildPermissionPacket::playerId),
                    ByteCodec.STRING.fieldOf(ClientboundModifyGuildPermissionPacket::permission),
                    ByteCodec.BOOLEAN.fieldOf(ClientboundModifyGuildPermissionPacket::value),
                    ClientboundModifyGuildPermissionPacket::new
                )
            );
        }

        @Override
        public Runnable handle(ClientboundModifyGuildPermissionPacket packet) {
            return () -> GuildApi.API.get(OdysseyAlliesClient.level(), packet.id()).ifPresent(guild ->
                GuildApi.API.modifyPermission(OdysseyAlliesClient.level(), guild, packet.playerId(), packet.permission(), packet.value()));
        }
    }
}
