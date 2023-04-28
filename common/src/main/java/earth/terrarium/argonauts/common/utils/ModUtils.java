package earth.terrarium.argonauts.common.utils;

import com.google.common.primitives.UnsignedInteger;
import com.mojang.authlib.GameProfile;

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
}
