package earth.terrarium.odyssey_guilds.client.compat.rei;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import earth.terrarium.odyssey_guilds.OdysseyGuilds;
import earth.terrarium.odyssey_guilds.client.screens.chat.ChatScreen;
import me.shedaniel.rei.api.client.favorites.FavoriteEntryType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("UnstableApiUsage")
public class PartyChatFavoriteEntry extends BaseFavoriteEntry {

    public static final ResourceLocation ID = OdysseyGuilds.id("party_chat");
    private static final ResourceLocation TEXTURE = OdysseyGuilds.id("textures/gui/icons/teamwork.png");

    public PartyChatFavoriteEntry() {
        super(ID, TEXTURE, ChatScreen::openParty);
    }

    public enum Type implements FavoriteEntryType<PartyChatFavoriteEntry> {
        INSTANCE;

        @Override
        public DataResult<PartyChatFavoriteEntry> read(CompoundTag object) {
            return DataResult.success(new PartyChatFavoriteEntry(), Lifecycle.stable());
        }

        @Override
        public DataResult<PartyChatFavoriteEntry> fromArgs(Object... args) {
            return DataResult.success(new PartyChatFavoriteEntry(), Lifecycle.stable());
        }

        @Override
        public CompoundTag save(PartyChatFavoriteEntry entry, CompoundTag tag) {
            return tag;
        }
    }
}