package earth.terrarium.argonauts.common.menus.party;

import com.teamresourceful.resourcefullib.common.menu.MenuContent;
import com.teamresourceful.resourcefullib.common.menu.MenuContentSerializer;
import it.unimi.dsi.fastutil.objects.Object2BooleanLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.minecraft.network.FriendlyByteBuf;

public record PartySettingsContent(boolean partySettings,
                                   Object2BooleanMap<String> settings) implements MenuContent<PartySettingsContent> {

    public static final MenuContentSerializer<PartySettingsContent> SERIALIZER = new Serializer();

    @Override
    public MenuContentSerializer<PartySettingsContent> serializer() {
        return SERIALIZER;
    }

    private static class Serializer implements MenuContentSerializer<PartySettingsContent> {

        @Override
        public PartySettingsContent from(FriendlyByteBuf buffer) {
            return new PartySettingsContent(buffer.readBoolean(), buffer.readMap(
                Object2BooleanLinkedOpenHashMap::new,
                FriendlyByteBuf::readUtf,
                FriendlyByteBuf::readBoolean));
        }

        @Override
        public void to(FriendlyByteBuf buffer, PartySettingsContent content) {
            buffer.writeBoolean(content.partySettings());
            buffer.writeMap(content.settings(), FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeBoolean);
        }
    }
}
