package earth.terrarium.odyssey_allies.common.network.packets;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import earth.terrarium.odyssey_allies.OdysseyAllies;
import earth.terrarium.odyssey_allies.api.teams.guild.GuildApi;
import earth.terrarium.odyssey_allies.api.teams.settings.Setting;
import earth.terrarium.odyssey_allies.client.OdysseyAlliesClient;

import java.util.UUID;

public record ClientboundModifyGuildSettingPacket(
    UUID id,
    Setting<?> setting,
    String settingId
) implements Packet<ClientboundModifyGuildSettingPacket> {

    public static final ClientboundPacketType<ClientboundModifyGuildSettingPacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundModifyGuildSettingPacket> type() {
        return TYPE;
    }

    private static class Type extends CodecPacketType<ClientboundModifyGuildSettingPacket> implements ClientboundPacketType<ClientboundModifyGuildSettingPacket> {

        public Type() {
            super(
                ClientboundModifyGuildSettingPacket.class,
                OdysseyAllies.id("modify_guild_setting"),
                ObjectByteCodec.create(
                    ByteCodec.UUID.fieldOf(ClientboundModifyGuildSettingPacket::id),
                    Setting.BYTE_CODEC.fieldOf(ClientboundModifyGuildSettingPacket::setting),
                    ByteCodec.STRING.fieldOf(ClientboundModifyGuildSettingPacket::settingId),
                    ClientboundModifyGuildSettingPacket::new
                )
            );
        }

        @Override
        public Runnable handle(ClientboundModifyGuildSettingPacket packet) {
            return () -> GuildApi.API.get(OdysseyAlliesClient.level(), packet.id()).ifPresent(guild ->
                GuildApi.API.modifySetting(OdysseyAlliesClient.level(), guild, packet.setting, packet.settingId));
        }
    }
}
