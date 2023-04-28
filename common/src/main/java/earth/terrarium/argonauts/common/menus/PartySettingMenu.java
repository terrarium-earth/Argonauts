package earth.terrarium.argonauts.common.menus;

import earth.terrarium.argonauts.common.registries.ModMenus;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class PartySettingMenu extends AbstractContainerMenu {

    private final PartySettingContent content;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public PartySettingMenu(int i, Inventory ignored, Optional<PartySettingContent> content) {
        this(i, content.orElse(null));
    }

    public PartySettingMenu(int id, PartySettingContent content) {
        super(ModMenus.PARTY_SETTINGS.get(), id);
        this.content = content;
    }

    public Object2BooleanMap<String> settings() {
        return content == null ? new Object2BooleanOpenHashMap<>() : content.settings();
    }

    public boolean isPartyScreen() {
        return content != null && content.partySettings();
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }
}
