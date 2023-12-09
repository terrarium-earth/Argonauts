package earth.terrarium.argonauts.client.compat.rei;

import me.shedaniel.rei.api.client.favorites.FavoriteEntryType;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import net.minecraft.network.chat.Component;

@SuppressWarnings("UnstableApiUsage")
public class ArgonautsReiClientPlugin implements REIClientPlugin {

    @Override
    public void registerFavorites(FavoriteEntryType.Registry registry) {
        registry.register(PartyChatFavoriteEntry.ID, PartyChatFavoriteEntry.Type.INSTANCE);
        registry.register(PartyManagementFavoriteEntry.ID, PartyManagementFavoriteEntry.Type.INSTANCE);
        registry.getOrCrateSection(Component.translatable("rei.sections.odyssey"))
            .add(
                new PartyChatFavoriteEntry(),
                new PartyManagementFavoriteEntry());
    }
}