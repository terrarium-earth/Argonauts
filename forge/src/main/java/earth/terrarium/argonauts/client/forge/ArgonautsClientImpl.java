package earth.terrarium.argonauts.client.forge;

import earth.terrarium.argonauts.client.ArgonautsClient;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class ArgonautsClientImpl {
    public static <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void register(MenuType<? extends M> type, ArgonautsClient.ScreenConstructor<M, U> factory) {
        MenuScreens.register(type, factory::create);
    }
}