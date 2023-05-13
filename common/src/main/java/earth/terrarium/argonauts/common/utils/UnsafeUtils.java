package earth.terrarium.argonauts.common.utils;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeUtils {

    private static final Unsafe UNSAFE;

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            UNSAFE = (Unsafe) theUnsafe.get(null);
        } catch (Exception e) {
            throw new RuntimeException("Unable to capture unsafe", e);
        }
    }

    @SuppressWarnings("deprecation")
    public static Object getStaticField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return UNSAFE.getObject(null, UNSAFE.staticFieldOffset(field));
        } catch (Exception e) {
            throw new RuntimeException("Unable to get static field", e);
        }
    }
}
