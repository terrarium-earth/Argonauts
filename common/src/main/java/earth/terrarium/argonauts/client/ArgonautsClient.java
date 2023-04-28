package earth.terrarium.argonauts.client;

import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.argonauts.client.screens.chat.CustomChatScreen;
import earth.terrarium.argonauts.client.screens.party.settings.PartySettingsScreen;
import earth.terrarium.argonauts.client.screens.party.members.PartyMembersScreen;
import earth.terrarium.argonauts.common.registries.ModMenus;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.ChatScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.apache.commons.lang3.NotImplementedException;

public class ArgonautsClient {

    public static void init() {
        register(ModMenus.PARTY.get(), PartyMembersScreen::new);
        register(ModMenus.PARTY_SETTINGS.get(), PartySettingsScreen::new);
        register(ModMenus.CHAT.get(), CustomChatScreen::new);
    }

    @ExpectPlatform
    public static <M extends AbstractContainerMenu, U extends Screen & MenuAccess<M>> void register(MenuType<? extends M> type, ScreenConstructor<M, U> factory) {
        throw new NotImplementedException();
    }

    @Environment(EnvType.CLIENT)
    public interface ScreenConstructor<T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> {
        U create(T menu, Inventory inventory, Component component);
    }
}
