package earth.terrarium.argonauts.common.menus;

import com.teamresourceful.resourcefullib.common.menu.MenuContent;
import com.teamresourceful.resourcefullib.common.menu.MenuContentSerializer;
import it.unimi.dsi.fastutil.objects.Object2BooleanLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.minecraft.network.FriendlyByteBuf;

public record PartySettingContent(boolean partySettings, Object2BooleanMap<String> settings) implements MenuContent<PartySettingContent> {

    public static final MenuContentSerializer<PartySettingContent> SERIALIZER = new Serializer();

    @Override
    public MenuContentSerializer<PartySettingContent> serializer() {
        return SERIALIZER;
    }

    private static class Serializer implements MenuContentSerializer<PartySettingContent> {

        @Override
        public PartySettingContent from(FriendlyByteBuf buffer) {
            return new PartySettingContent(buffer.readBoolean(), buffer.readMap(
                Object2BooleanLinkedOpenHashMap::new,
                FriendlyByteBuf::readUtf,
                FriendlyByteBuf::readBoolean));
        }

        @Override
        public void to(FriendlyByteBuf buffer, PartySettingContent content) {
            buffer.writeBoolean(content.partySettings());
            buffer.writeMap(content.settings(), FriendlyByteBuf::writeUtf, FriendlyByteBuf::writeBoolean);
        }
    }
}
