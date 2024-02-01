package earth.terrarium.argonauts.common.network.messages;

import com.teamresourceful.resourcefullib.common.network.Packet;
import com.teamresourceful.resourcefullib.common.network.base.ClientboundPacketType;
import com.teamresourceful.resourcefullib.common.network.base.PacketType;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.party.settings.PartySettingsScreen;
import earth.terrarium.argonauts.common.menus.party.PartySettingsContent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record ClientboundOpenPartySettingsMenuPacket(PartySettingsContent menuContent,
                                                     Component displayName) implements Packet<ClientboundOpenPartySettingsMenuPacket> {

    public static final ClientboundPacketType<ClientboundOpenPartySettingsMenuPacket> TYPE = new Type();

    @Override
    public PacketType<ClientboundOpenPartySettingsMenuPacket> type() {
        return TYPE;
    }

    private static class Type implements ClientboundPacketType<ClientboundOpenPartySettingsMenuPacket> {

        @Override
        public Class<ClientboundOpenPartySettingsMenuPacket> type() {
            return ClientboundOpenPartySettingsMenuPacket.class;
        }

        @Override
        public ResourceLocation id() {
            return new ResourceLocation(Argonauts.MOD_ID, "open_party_settings_menu");
        }

        @Override
        public void encode(ClientboundOpenPartySettingsMenuPacket message, FriendlyByteBuf buffer) {
            message.menuContent.serializer().to(buffer, message.menuContent);
            buffer.writeComponent(message.displayName);
        }

        @Override
        public ClientboundOpenPartySettingsMenuPacket decode(FriendlyByteBuf buffer) {
            return new ClientboundOpenPartySettingsMenuPacket(
                PartySettingsContent.SERIALIZER.from(buffer),
                buffer.readComponent()
            );
        }

        @Override
        public Runnable handle(ClientboundOpenPartySettingsMenuPacket packet) {
            return () -> PartySettingsScreen.open(packet.menuContent, packet.displayName);
        }
    }
}
