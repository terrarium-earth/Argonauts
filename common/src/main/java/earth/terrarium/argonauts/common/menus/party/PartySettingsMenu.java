package earth.terrarium.argonauts.common.menus.party;

import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PartySettingsMenu extends AbstractContainerMenu {

    private final PartySettingsContent content;

    public PartySettingsMenu(int id, PartySettingsContent content) {
        super(null, id);
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
