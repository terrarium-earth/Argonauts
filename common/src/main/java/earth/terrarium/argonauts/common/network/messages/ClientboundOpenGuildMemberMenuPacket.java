package earth.terrarium.argonauts.common.network.messages;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.guild.members.GuildMembersScreen;
import earth.terrarium.argonauts.common.menus.guild.GuildMembersContent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record ClientboundOpenGuildMemberMenuPacket(GuildMembersContent menuContent,
                                                   Component displayName) implements Packet<ClientboundOpenGuildMemberMenuPacket> {

    public static final ClientboundPacketType<ClientboundOpenGuildMemberMenuPacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundOpenGuildMemberMenuPacket> type() {
        return TYPE;
    }

    private static class Type implements ClientboundPacketType<ClientboundOpenGuildMemberMenuPacket> {

        @Override
        public Class<ClientboundOpenGuildMemberMenuPacket> type() {
            return ClientboundOpenGuildMemberMenuPacket.class;
        }

        @Override
        public ResourceLocation id() {
            return new ResourceLocation(Argonauts.MOD_ID, "open_guild_member_menu");
        }

        @Override
        public void encode(ClientboundOpenGuildMemberMenuPacket message, FriendlyByteBuf buffer) {
            message.menuContent.serializer().to(buffer, message.menuContent);
            buffer.writeComponent(message.displayName);
        }

        @Override
        public ClientboundOpenGuildMemberMenuPacket decode(FriendlyByteBuf buffer) {
            return new ClientboundOpenGuildMemberMenuPacket(
                (GuildMembersContent) GuildMembersContent.SERIALIZER.from(buffer),
                buffer.readComponent()
            );
        }

        @Override
        public Runnable handle(ClientboundOpenGuildMemberMenuPacket packet) {
            return () -> GuildMembersScreen.open(packet.menuContent, packet.displayName);
        }
    }
}
