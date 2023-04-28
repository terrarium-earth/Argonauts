package earth.terrarium.argonauts.common.menus;

import com.google.common.primitives.UnsignedInteger;
import earth.terrarium.argonauts.common.handlers.chat.ChatMessage;
import earth.terrarium.argonauts.common.handlers.chat.ChatMessageType;
import earth.terrarium.argonauts.common.registries.ModMenus;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

public class ChatMenu extends AbstractContainerMenu {

    private final ChatContent content;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public ChatMenu(int i, Inventory ignored, Optional<ChatContent> content) {
        this(i, content.orElse(null));
    }

    public ChatMenu(int id, ChatContent content) {
        super(ModMenus.CHAT.get(), id);
        this.content = content;
    }

    public List<String> usernames() {
        return content == null ? List.of() : content.usernames();
    }

    public int maxUsers() {
        return content == null ? 0 : content.maxUsers();
    }

    public ChatMessageType type() {
        return content == null ? ChatMessageType.UNKNOWN : content.type();
    }

    public LinkedHashMap<UnsignedInteger, ChatMessage> messages() {
        return content == null ? new LinkedHashMap<>() : content.messages();
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
