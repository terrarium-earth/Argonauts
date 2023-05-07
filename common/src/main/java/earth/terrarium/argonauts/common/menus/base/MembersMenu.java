package earth.terrarium.argonauts.common.menus.base;

import com.mojang.authlib.GameProfile;
import com.teamresourceful.resourcefullib.common.registry.RegistryEntry;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public abstract class MembersMenu extends AbstractContainerMenu {

    private final MembersContent content;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public MembersMenu(RegistryEntry<MenuType<MembersMenu>> menu, int i, Inventory ignored, Optional<MembersContent> content) {
        this(menu, i, content.orElse(null));
    }

    public MembersMenu(RegistryEntry<MenuType<MembersMenu>> menu, int id, MembersContent content) {
        super(menu.get(), id);
        this.content = content;
    }

    public List<? extends Member> members() {
        return content == null ? List.of() : content.members();
    }

    public boolean canManageMembers() {
        return content != null && content.canManageMembers();
    }

    public boolean canManagePermissions() {
        return content != null && content.canManagePermissions();
    }

    public Member getSelected() {
        if (content != null && content.selected() >= 0 && content.selected() < content.members().size()) {
            return content.members().get(content.selected());
        }
        return null;
    }

    public Member getSelf() {
        if (content != null) {
            for (Member member : content.members()) {
                if (ModUtils.areProfilesSame(member.profile(), Minecraft.getInstance().getUser().getGameProfile())) {
                    return member;
                }
            }
        }
        return null;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    @Override
    public boolean clickMenuButton(@NotNull Player p, int i) {
        if (p instanceof ServerPlayer player) {
            try {
                openScreen(player, i);
            } catch (MemberException ignored) {}
        }
        return super.clickMenuButton(p, i);
    }

    public abstract void openScreen(ServerPlayer player, int i) throws MemberException;

    public OptionalInt getId(GameProfile profile) {
        if (content != null && profile != null) {
            for (int i = 0; i < this.members().size(); i++) {
                Member member = this.members().get(i);
                if (member.profile().equals(profile)) {
                    return OptionalInt.of(i);
                }
            }
        }
        return OptionalInt.empty();
    }
}
