package earth.terrarium.argonauts.client.screens.base.members;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.screens.AbstractContainerCursorScreen;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.base.members.entries.*;
import earth.terrarium.argonauts.client.utils.ClientUtils;
import earth.terrarium.argonauts.client.utils.MouseLocationFix;
import earth.terrarium.argonauts.common.handlers.base.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import earth.terrarium.argonauts.common.handlers.base.members.MemberState;
import earth.terrarium.argonauts.common.menus.base.MembersMenu;
import net.minecraft.Optionull;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class MembersScreen extends AbstractContainerCursorScreen<MembersMenu> {

    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(Argonauts.MOD_ID, "textures/gui/members.png");

    public MembersScreen(MembersMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.passEvents = false;
        this.imageHeight = 223;
        this.imageWidth = 276;
    }

    @Override
    protected void init() {
        MouseLocationFix.fix(this.getClass());
        super.init();

        addRenderableWidget(new MembersList(this.leftPos + 8, this.topPos + 29, 70, 180, 20, item ->
            this.menu.getId(Optionull.map(item, MembersList.Entry::profile))
                .ifPresent(id -> ClientUtils.sendClick(this, id)))).update(this.menu.members());

        var list = addRenderableWidget(new MemberSettingList(this.leftPos + 84, this.topPos + 29, 184, 180));

        Member member = this.menu.getSelected();
        Member self = this.menu.getSelf();
        if (member != null && self != null) {
            boolean cantModify = member.equals(self) || member.getState().isLeader();

            Component status = member.getState() == MemberState.INVITED ? Component.translatable("argonauts.member.status.invited") : Component.translatable("argonauts.member.status.accepted");
            list.addEntry(new TextEntry(Component.translatable("argonauts.member.status.text"), status));

            list.addEntry(new DividerEntry(Component.translatable("argonauts.member.settings")));

            var entry = new RoleNameEntry(!cantModify);
            list.addEntry(entry);
            entry.setText(member.getRole());

            list.addEntry(new DividerEntry(Component.translatable("argonauts.member.permissions")));
            List<String> leftOver = new ArrayList<>(member.permissions());
            leftOver.remove(MemberPermissions.TEMPORARY_GUILD_PERMISSIONS);
            for (String permission : MemberPermissions.ALL_PERMISSIONS) {
                list.addEntry(new BooleanEntry(permission, member.hasPermission(permission), !cantModify && this.menu.canManagePermissions() && self.hasPermission(permission)));
                leftOver.remove(permission);
            }

            for (String permission : leftOver) {
                list.addEntry(new BooleanEntry(permission, true, !cantModify && this.menu.canManagePermissions() && self.hasPermission(permission)));
            }

            additionalEntries(list, member, cantModify, self);

            list.addEntry(new DividerEntry(Component.translatable("argonauts.member.actions")));
            list.addEntry(new CommandEntry(
                Component.translatable("argonauts.member.remove"),
                Component.translatable("argonauts.member.remove.button"),
                runRemoveCommand(member),
                !cantModify && this.menu.canManageMembers()
            ));
        }
    }

    public void additionalEntries(MemberSettingList list, Member member, boolean cantModify, Member self) {}

    public abstract String runRemoveCommand(Member member);

    @Override
    public void render(@NotNull PoseStack stack, int i, int j, float f) {
        this.renderBackground(stack);
        super.render(stack, i, j, f);
        this.renderTooltip(stack, i, j);
    }

    @Override
    protected void renderLabels(@NotNull PoseStack stack, int i, int j) {
        this.font.draw(stack, title, (float) this.titleLabelX, (float) this.titleLabelY, 0x404040);
    }

    @Override
    protected void renderBg(@NotNull PoseStack stack, float f, int i, int j) {
        RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        blit(stack, k, l, 0, 0, this.imageWidth, this.imageHeight, 512, 512);
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        if (Minecraft.getInstance().options.keyInventory.matches(i, j)) {
            return true;
        }
        return super.keyPressed(i, j, k);
    }

    @Override
    public void resize(Minecraft minecraft, int i, int j) {
        super.resize(minecraft, i, j);
    }
}
