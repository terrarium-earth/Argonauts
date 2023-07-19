package earth.terrarium.argonauts.common.network.messages;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.party.members.PartyMembersScreen;
import earth.terrarium.argonauts.common.menus.party.PartyMembersContent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record ClientboundOpenPartyMemberMenuPacket(PartyMembersContent menuContent,
                                                   Component displayName) implements Packet<ClientboundOpenPartyMemberMenuPacket> {

    public static final ResourceLocation ID = new ResourceLocation(Argonauts.MOD_ID, "open_party_member_menu");
    public static final PacketHandler<ClientboundOpenPartyMemberMenuPacket> HANDLER = new Handler();

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<ClientboundOpenPartyMemberMenuPacket> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<ClientboundOpenPartyMemberMenuPacket> {

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
        public PacketContext handle(ClientboundOpenPartyMemberMenuPacket message) {
            return (player, level) -> PartyMembersScreen.open(message.menuContent, message.displayName);
        }
    }
}
