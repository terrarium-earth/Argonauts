package earth.terrarium.odyssey_allies.common.network.packets;

import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import com.teamresourceful.resourcefullib.common.network.defaults.CodecPacketType;
import earth.terrarium.odyssey_allies.OdysseyAllies;
import earth.terrarium.odyssey_allies.api.teams.guild.Guild;
import earth.terrarium.odyssey_allies.api.teams.guild.GuildApi;
import earth.terrarium.odyssey_allies.client.OdysseyAlliesClient;

public record ClientboundAddGuildPacket(
    Guild guild
) implements Packet<ClientboundAddGuildPacket> {

    public static final ClientboundPacketType<ClientboundAddGuildPacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundAddGuildPacket> type() {
        return TYPE;
    }

    private static class Type extends CodecPacketType<ClientboundAddGuildPacket> implements ClientboundPacketType<ClientboundAddGuildPacket> {

        public Type() {
            super(
                ClientboundAddGuildPacket.class,
                OdysseyAllies.id("add_guild"),
                ObjectByteCodec.create(
                    Guild.BYTE_CODEC.fieldOf(ClientboundAddGuildPacket::guild),
                    ClientboundAddGuildPacket::new
                )
            );
        }

        @Override
        public Runnable handle(ClientboundAddGuildPacket packet) {
            return () -> GuildApi.API.create(OdysseyAlliesClient.level(), packet.guild);
        }
    }
}
