package earth.terrarium.argonauts.common.utils;

import com.google.common.primitives.UnsignedInteger;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

import java.util.function.Predicate;
import java.util.function.Supplier;

public final class ModUtils {

    public static final UnsignedInteger UNSIGNED_TWO = UnsignedInteger.valueOf(2);

    public static <T> T generate(Predicate<T> validator, Supplier<T> getter) {
        T value;
        do {
            value = getter.get();
        } while (!validator.test(value));
        return value;
    }

    public static boolean areProfilesSame(GameProfile first, GameProfile second) {
        if (first == null || second == null) {
            return false;
        }
        if (first.getId() != null && second.getId() != null) {
            return first.getId().equals(second.getId());
        }
        return first.getName() != null && second.getName() != null && first.getName().equals(second.getName());
    }

    public static GlobalPos readGlobalPos(CompoundTag tag) {
        ResourceLocation key = ResourceLocation.tryParse(tag.getString("dimension"));
        if (key == null) {
            return null;
        }
        BlockPos pos = BlockPos.of(tag.getLong("pos"));
        ResourceKey<Level> level = ResourceKey.create(Registries.DIMENSION, key);
        return GlobalPos.of(level, pos);
    }

    public static CompoundTag writeGlobalPos(GlobalPos pos) {
        CompoundTag tag = new CompoundTag();
        tag.putString("dimension", pos.dimension().location().toString());
        tag.putLong("pos", pos.pos().asLong());
        return tag;
    }

    public static GameProfile readBasicProfile(CompoundTag tag) {
        return new GameProfile(tag.getUUID("id"), tag.getString("name"));
    }

    public static CompoundTag writeBasicProfile(GameProfile profile) {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("id", profile.getId());
        tag.putString("name", profile.getName());
        return tag;
    }
}
