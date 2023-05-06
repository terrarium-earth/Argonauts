package earth.terrarium.argonauts.common.registries;

import com.teamresourceful.resourcefullib.common.menu.MenuContentHelper;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistries;
import com.teamresourceful.resourcefullib.common.registry.ResourcefulRegistry;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.common.menus.*;
import earth.terrarium.argonauts.common.menus.party.PartyMembersContent;
import earth.terrarium.argonauts.common.menus.party.PartyMembersMenu;
import earth.terrarium.argonauts.common.menus.party.PartySettingsContent;
import earth.terrarium.argonauts.common.menus.party.PartySettingsMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;

public class ModMenus {

    public static final ResourcefulRegistry<MenuType<?>> MENUS = ResourcefulRegistries.create(BuiltInRegistries.MENU, Argonauts.MOD_ID);

    public static final RegistryEntry<MenuType<PartyMembersMenu>> PARTY = MENUS.register("party", () -> MenuContentHelper.create(PartyMembersMenu::new, PartyMembersContent.SERIALIZER));
    public static final RegistryEntry<MenuType<PartySettingsMenu>> PARTY_SETTINGS = MENUS.register("party_settings", () -> MenuContentHelper.create(PartySettingsMenu::new, PartySettingsContent.SERIALIZER));
    public static final RegistryEntry<MenuType<ChatMenu>> CHAT = MENUS.register("chat", () -> MenuContentHelper.create(ChatMenu::new, ChatContent.SERIALIZER));
}
