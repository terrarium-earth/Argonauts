package earth.terrarium.argonauts.common.menus;

import com.mojang.authlib.GameProfile;
import earth.terrarium.argonauts.common.commands.party.PartyMemberCommands;
import earth.terrarium.argonauts.common.handlers.party.members.PartyMember;
import earth.terrarium.argonauts.common.registries.ModMenus;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

public class PartyMembersMenu extends AbstractContainerMenu {

    private final PartyMembersContent content;

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public PartyMembersMenu(int i, Inventory ignored, Optional<PartyMembersContent> content) {
        this(i, content.orElse(null));
    }

    public PartyMembersMenu(int id, PartyMembersContent content) {
        super(ModMenus.PARTY.get(), id);
        this.content = content;
    }

    public List<PartyMember> members() {
        return content == null ? List.of() : content.members();
    }

    public boolean canManageMembers() {
        return content != null && content.canManageMembers();
    }

    public boolean canManagePermissions() {
        return content != null && content.canManagePermissions();
    }

    public PartyMember getSelected() {
        if (content != null && content.selected() >= 0 && content.selected() < content.members().size()) {
            return content.members().get(content.selected());
        }
        return null;
    }

    public PartyMember getSelf() {
        if (content != null) {
            for (PartyMember member : content.members()) {
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
                PartyMemberCommands.openMembersScreen(player, i);
            } catch (Exception ignored) {}
        }
        return super.clickMenuButton(p, i);
    }

    public OptionalInt getId(GameProfile profile) {
        if (content != null && profile != null) {
            for (int i = 0; i < this.members().size(); i++) {
                PartyMember member = this.members().get(i);
                if (member.profile().equals(profile)) {
                    return OptionalInt.of(i);
                }
            }
        }
        return OptionalInt.empty();
    }
}
