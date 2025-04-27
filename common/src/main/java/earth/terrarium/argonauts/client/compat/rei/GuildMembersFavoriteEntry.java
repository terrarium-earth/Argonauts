package earth.terrarium.argonauts.client.compat.rei;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.members.MembersScreen;
import me.shedaniel.rei.api.client.favorites.FavoriteEntryType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("UnstableApiUsage")
public class GuildMembersFavoriteEntry extends BaseFavoriteEntry {

    public static final ResourceLocation ID = Argonauts.id("guild_members");
    private static final ResourceLocation TEXTURE = Argonauts.id("textures/gui/icons/chat.png");

    public GuildMembersFavoriteEntry() {
        super(ID, TEXTURE, MembersScreen::openGuild);
    }

    public enum Type implements FavoriteEntryType<GuildMembersFavoriteEntry> {
        INSTANCE;

        @Override
        public DataResult<GuildMembersFavoriteEntry> read(CompoundTag object) {
            return DataResult.success(new GuildMembersFavoriteEntry(), Lifecycle.stable());
        }

        @Override
        public DataResult<GuildMembersFavoriteEntry> fromArgs(Object... args) {
            return DataResult.success(new GuildMembersFavoriteEntry(), Lifecycle.stable());
        }

        @Override
        public CompoundTag save(GuildMembersFavoriteEntry entry, CompoundTag tag) {
            return tag;
        }
    }
}