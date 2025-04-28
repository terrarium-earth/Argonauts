package earth.terrarium.odyssey_allies.client.compat.rei;

import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import earth.terrarium.odyssey_allies.OdysseyAllies;
import earth.terrarium.odyssey_allies.client.screens.members.MembersScreen;
import me.shedaniel.rei.api.client.favorites.FavoriteEntryType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("UnstableApiUsage")
public class PartyMembersFavoriteEntry extends BaseFavoriteEntry {

    public static final ResourceLocation ID = OdysseyAllies.id("party_management");
    private static final ResourceLocation TEXTURE = OdysseyAllies.id("textures/gui/icons/chat.png");

    public PartyMembersFavoriteEntry() {
        super(ID, TEXTURE, MembersScreen::openParty);
    }

    public enum Type implements FavoriteEntryType<PartyMembersFavoriteEntry> {
        INSTANCE;

        @Override
        public DataResult<PartyMembersFavoriteEntry> read(CompoundTag object) {
            return DataResult.success(new PartyMembersFavoriteEntry(), Lifecycle.stable());
        }

        @Override
        public DataResult<PartyMembersFavoriteEntry> fromArgs(Object... args) {
            return DataResult.success(new PartyMembersFavoriteEntry(), Lifecycle.stable());
        }

        @Override
        public CompoundTag save(PartyMembersFavoriteEntry entry, CompoundTag tag) {
            return tag;
        }
    }
}