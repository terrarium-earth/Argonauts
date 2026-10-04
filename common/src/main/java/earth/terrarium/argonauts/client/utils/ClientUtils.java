package earth.terrarium.argonauts.client.utils;

import it.unimi.dsi.fastutil.chars.Char2CharMap;
import it.unimi.dsi.fastutil.chars.Char2CharOpenHashMap;
import net.minecraft.Util;

public class ClientUtils {

    private static final Char2CharMap SMALL_NUMBERS = Util.make(new Char2CharOpenHashMap(), map -> {
        map.put('0', '₀');
        map.put('1', '₁');
        map.put('2', '₂');
        map.put('3', '₃');
        map.put('4', '₄');
        map.put('5', '₅');
        map.put('6', '₆');
        map.put('7', '₇');
        map.put('8', '₈');
        map.put('9', '₉');
    });

    public static String getSmallNumber(int num) {
        String normal = String.valueOf(num);
        StringBuilder builder = new StringBuilder(normal.length());
        for (char c : String.valueOf(num).toCharArray()) {
            builder.append(SMALL_NUMBERS.getOrDefault(c, c));
        }
        return builder.toString();
    }
}
