package earth.terrarium.argonauts.common.network.packets;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.teams.party.PartyApi;
import earth.terrarium.argonauts.api.teams.settings.Setting;
import earth.terrarium.argonauts.client.ArgonautsClient;
import net.minecraft.resources.ResourceLocation;

import java.util.UUID;

public record ClientboundModifyPartySettingPacket(
    UUID id,
    Setting<?> setting,
    String settingId
) implements Packet<ClientboundModifyPartySettingPacket> {

    public static final ClientboundPacketType<ClientboundModifyPartySettingPacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundModifyPartySettingPacket> type() {
        return TYPE;
    }

    private static class Type extends CodecPacketType<ClientboundModifyPartySettingPacket> implements ClientboundPacketType<ClientboundModifyPartySettingPacket> {

        public Type() {
            super(
                ClientboundModifyPartySettingPacket.class,
                Argonauts.id("modify_party_setting"),
                ObjectByteCodec.create(
                    ByteCodec.UUID.fieldOf(ClientboundModifyPartySettingPacket::id),
                    Setting.BYTE_CODEC.fieldOf(ClientboundModifyPartySettingPacket::setting),
                    ByteCodec.STRING.fieldOf(ClientboundModifyPartySettingPacket::settingId),
                    ClientboundModifyPartySettingPacket::new
                )
            );
        }

        @Override
        public Runnable handle(ClientboundModifyPartySettingPacket packet) {
            return () -> PartyApi.API.get(packet.id()).ifPresent(party ->
                PartyApi.API.modifySetting(ArgonautsClient.level(), party, packet.setting, packet.settingId));
        }
    }
}
