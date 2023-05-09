package earth.terrarium.argonauts.client.utils;

import it.unimi.dsi.fastutil.chars.Char2CharMap;
import it.unimi.dsi.fastutil.chars.Char2CharOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;

import java.net.http.HttpClient;
import java.util.List;
import java.util.Objects;

public class ClientUtils {

    public static final HttpClient WEB = HttpClient.newBuilder().build();

    private static final Char2CharMap SMALL_NUMBERS = new Char2CharOpenHashMap();

    static {
        SMALL_NUMBERS.put('0', '₀');
        SMALL_NUMBERS.put('1', '₁');
        SMALL_NUMBERS.put('2', '₂');
        SMALL_NUMBERS.put('3', '₃');
        SMALL_NUMBERS.put('4', '₄');
        SMALL_NUMBERS.put('5', '₅');
        SMALL_NUMBERS.put('6', '₆');
        SMALL_NUMBERS.put('7', '₇');
        SMALL_NUMBERS.put('8', '₈');
        SMALL_NUMBERS.put('9', '₉');
    }

    public static void setTooltip(Component component) {
        if (Minecraft.getInstance().screen != null) {
            Minecraft.getInstance().screen.setTooltipForNextRenderPass(List.of(component.getVisualOrderText()));
        }
    }

    public static void sendCommand(String command) {
        Objects.requireNonNull(Minecraft.getInstance().getConnection()).sendUnsignedCommand(command);
    }

    public static void sendClick(AbstractContainerScreen<?> screen, int content) {
        if (Minecraft.getInstance().gameMode != null) {
            Minecraft.getInstance().gameMode.handleInventoryButtonClick(screen.getMenu().containerId, content);
        }
    }

    public static String getSmallNumber(int num) {
        String normal = String.valueOf(num);
        StringBuilder builder = new StringBuilder(normal.length());
        for (char c : String.valueOf(num).toCharArray()) {
            builder.append(SMALL_NUMBERS.getOrDefault(c, c));
        }
        return builder.toString();
    }
}
