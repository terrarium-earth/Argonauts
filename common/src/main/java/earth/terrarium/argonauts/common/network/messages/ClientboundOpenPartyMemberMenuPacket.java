package earth.terrarium.argonauts.common.network.messages;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.party.members.PartyMembersScreen;
import earth.terrarium.argonauts.common.menus.party.PartyMembersContent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record ClientboundOpenPartyMemberMenuPacket(PartyMembersContent menuContent,
                                                   Component displayName) implements Packet<ClientboundOpenPartyMemberMenuPacket> {

    public static final ClientboundPacketType<ClientboundOpenPartyMemberMenuPacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundOpenPartyMemberMenuPacket> type() {
        return TYPE;
    }

    private static class Type implements ClientboundPacketType<ClientboundOpenPartyMemberMenuPacket> {

        @Override
        public Class<ClientboundOpenPartyMemberMenuPacket> type() {
            return ClientboundOpenPartyMemberMenuPacket.class;
        }

        @Override
        public ResourceLocation id() {
            return new ResourceLocation(Argonauts.MOD_ID, "open_party_member_menu");
        }

        @Override
        public void encode(ClientboundOpenPartyMemberMenuPacket message, FriendlyByteBuf buffer) {
            message.menuContent.serializer().to(buffer, message.menuContent);
            buffer.writeComponent(message.displayName);
        }

        @Override
        public ClientboundOpenPartyMemberMenuPacket decode(FriendlyByteBuf buffer) {
            return new ClientboundOpenPartyMemberMenuPacket(
                (PartyMembersContent) PartyMembersContent.SERIALIZER.from(buffer),
                buffer.readComponent()
            );
        }

        @Override
        public Runnable handle(ClientboundOpenPartyMemberMenuPacket packet) {
            return () -> PartyMembersScreen.open(packet.menuContent, packet.displayName);
        }
    }
}
