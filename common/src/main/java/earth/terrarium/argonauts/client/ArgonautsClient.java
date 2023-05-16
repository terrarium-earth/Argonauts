package earth.terrarium.argonauts.client;

import com.mojang.blaze3d.platform.InputConstants;
import earth.terrarium.argonauts.client.screens.chat.CustomChatScreen;
import earth.terrarium.argonauts.client.screens.guild.members.GuildMembersScreen;
import earth.terrarium.argonauts.client.screens.party.members.guild.members.PartyMembersScreen;
import earth.terrarium.argonauts.client.screens.party.settings.PartySettingsScreen;
import earth.terrarium.argonauts.client.utils.ClientUtils;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import earth.terrarium.argonauts.common.registries.ModMenus;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

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
        ClientUtils.register(ModMenus.PARTY.get(), PartyMembersScreen::new);
        ClientUtils.register(ModMenus.GUILD.get(), GuildMembersScreen::new);
        ClientUtils.register(ModMenus.PARTY_SETTINGS.get(), PartySettingsScreen::new);
        ClientUtils.register(ModMenus.CHAT.get(), CustomChatScreen::new);
    }

    public static void clientTick() {
        if (KEY_OPEN_PARTY_CHAT.consumeClick()) {
            ClientUtils.sendCommand("party chat");
        }
        if (KEY_OPEN_GUILD_CHAT.consumeClick()) {
            ClientUtils.sendCommand("guild chat");
        }
    }

    @Environment(EnvType.CLIENT)
    public interface ScreenConstructor<T extends AbstractContainerMenu, U extends Screen & MenuAccess<T>> {
        U create(T menu, Inventory inventory, Component component);
    }
}
