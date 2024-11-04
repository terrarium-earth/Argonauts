package earth.terrarium.argonauts.client.compat.rei;

import me.shedaniel.rei.api.client.favorites.FavoriteEntryType;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import net.minecraft.network.chat.Component;

@SuppressWarnings("UnstableApiUsage")
public class ArgonautsReiClientPlugin implements REIClientPlugin {

    @Override
    public void registerFavorites(FavoriteEntryType.Registry registry) {
        registry.register(PartyChatFavoriteEntry.ID, PartyChatFavoriteEntry.Type.INSTANCE);
        registry.register(GuildChatFavoriteEntry.ID, GuildChatFavoriteEntry.Type.INSTANCE);
        registry.register(PartyMembersFavoriteEntry.ID, PartyMembersFavoriteEntry.Type.INSTANCE);
        registry.register(GuildMembersFavoriteEntry.ID, GuildMembersFavoriteEntry.Type.INSTANCE);
        registry.getOrCrateSection(Component.translatable("rei.sections.project_odyssey"))
            .add(
                new PartyChatFavoriteEntry(),
                new PartyMembersFavoriteEntry());
    }
}