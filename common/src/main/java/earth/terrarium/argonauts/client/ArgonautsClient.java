package earth.terrarium.argonauts.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.teamresourceful.resourcefullib.client.utils.ScreenUtils;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class ArgonautsClient {

    public static final KeyMapping KEY_OPEN_PARTY_CHAT = new KeyMapping(
        ConstantComponents.KEY_OPEN_PARTY_CHAT.getString(),
        InputConstants.UNKNOWN.getValue(),
        ConstantComponents.ODYSSEY_CATEGORY.getString());
    public static final KeyMapping KEY_OPEN_GUILD_CHAT = new KeyMapping(
        ConstantComponents.KEY_OPEN_GUILD_CHAT.getString(),
        InputConstants.UNKNOWN.getValue(),
        ConstantComponents.ODYSSEY_CATEGORY.getString());

    public static void init() {
    }

    public static <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void register(MenuType<? extends M> type, MenuScreens.ScreenConstructor<M, U> factory) {
        MenuScreens.register(type, factory);
    }

    public static void clientTick() {
        if (KEY_OPEN_PARTY_CHAT.consumeClick()) {
            ScreenUtils.sendCommand("party chat");
        }
        if (KEY_OPEN_GUILD_CHAT.consumeClick()) {
            ScreenUtils.sendCommand("guild chat");
        }
    }
}
