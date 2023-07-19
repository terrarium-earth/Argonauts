package earth.terrarium.argonauts.common.network.messages;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.guild.members.GuildMembersScreen;
import earth.terrarium.argonauts.common.menus.guild.GuildMembersContent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record ClientboundOpenGuildMemberMenuPacket(GuildMembersContent menuContent,
                                                   Component displayName) implements Packet<ClientboundOpenGuildMemberMenuPacket> {

    public static final ResourceLocation ID = new ResourceLocation(Argonauts.MOD_ID, "open_guild_member_menu");
    public static final PacketHandler<ClientboundOpenGuildMemberMenuPacket> HANDLER = new Handler();

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<ClientboundOpenGuildMemberMenuPacket> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<ClientboundOpenGuildMemberMenuPacket> {

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
        public PacketContext handle(ClientboundOpenGuildMemberMenuPacket message) {
            return (player, level) -> GuildMembersScreen.open(message.menuContent, message.displayName);
        }
    }
}
