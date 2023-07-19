package earth.terrarium.argonauts.common.network.messages;

import com.teamresourceful.resourcefullib.common.networking.base.Packet;
import com.teamresourceful.resourcefullib.common.networking.base.PacketContext;
import com.teamresourceful.resourcefullib.common.networking.base.PacketHandler;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.party.settings.PartySettingsScreen;
import earth.terrarium.argonauts.common.menus.party.PartySettingsContent;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record ClientboundOpenPartySettingsMenuPacket(PartySettingsContent menuContent,
                                                     Component displayName) implements Packet<ClientboundOpenPartySettingsMenuPacket> {

    public static final ResourceLocation ID = new ResourceLocation(Argonauts.MOD_ID, "open_party_settings_menu");
    public static final PacketHandler<ClientboundOpenPartySettingsMenuPacket> HANDLER = new Handler();

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public PacketHandler<ClientboundOpenPartySettingsMenuPacket> getHandler() {
        return HANDLER;
    }

    private static class Handler implements PacketHandler<ClientboundOpenPartySettingsMenuPacket> {

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
        public PacketContext handle(ClientboundOpenPartySettingsMenuPacket message) {
            return (player, level) -> PartySettingsScreen.open(message.menuContent, message.displayName);
        }
    }
}
