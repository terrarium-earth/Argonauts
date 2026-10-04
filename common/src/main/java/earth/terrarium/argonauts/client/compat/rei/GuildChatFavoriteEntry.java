package earth.terrarium.argonauts.client.compat.rei;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.chat.ChatScreen;
import me.shedaniel.rei.api.client.favorites.FavoriteEntryType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("UnstableApiUsage")
public class GuildChatFavoriteEntry extends BaseFavoriteEntry {

    public static final ResourceLocation ID = Argonauts.id("guild_chat");
    private static final ResourceLocation TEXTURE = Argonauts.id("textures/gui/icons/teamwork.png");

    public GuildChatFavoriteEntry() {
        super(ID, TEXTURE, ChatScreen::openGuild);
    }

    public enum Type implements FavoriteEntryType<GuildChatFavoriteEntry> {
        INSTANCE;

        @Override
        public DataResult<GuildChatFavoriteEntry> read(CompoundTag object) {
            return DataResult.success(new GuildChatFavoriteEntry(), Lifecycle.stable());
        }

        @Override
        public DataResult<GuildChatFavoriteEntry> fromArgs(Object... args) {
            return DataResult.success(new GuildChatFavoriteEntry(), Lifecycle.stable());
        }

        @Override
        public CompoundTag save(GuildChatFavoriteEntry entry, CompoundTag tag) {
            return tag;
        }
    }
}