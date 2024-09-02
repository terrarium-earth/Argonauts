package earth.terrarium.argonauts.client.screens.members;

import com.mojang.authlib.GameProfile;
import com.teamresourceful.resourcefullib.client.utils.ScreenUtils;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.teams.Member;
import earth.terrarium.argonauts.api.teams.Team;
import earth.terrarium.argonauts.api.teams.guild.GuildApi;
import earth.terrarium.argonauts.api.teams.party.PartyApi;
import earth.terrarium.argonauts.api.teams.permissions.MemberPermissionsApi;
import earth.terrarium.argonauts.client.screens.BaseScreen;
import earth.terrarium.argonauts.client.screens.entries.BooleanEntry;
import earth.terrarium.argonauts.client.screens.entries.CommandEntry;
import earth.terrarium.argonauts.client.screens.entries.DividerEntry;
import earth.terrarium.argonauts.client.screens.entries.TextEntry;
import earth.terrarium.argonauts.client.screens.settings.SettingList;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class MembersScreen extends BaseScreen {

    private static final ResourceLocation CONTAINER_BACKGROUND = Argonauts.id("textures/gui/members.png");

    private final Team team;
    private final Set<String> permissions;
    private final List<PlayerInfo> members = new ArrayList<>();

    private final UUID selfId;

    @Nullable
    private GameProfile selectedProfile;
    @Nullable
    private Member selectedMember;

    public MembersScreen(Component displayName, Team team, Set<String> permissions) {
        super(displayName, 276, 223);
        this.team = team;
        this.permissions = permissions;
        this.selfId = Minecraft.getInstance().getGameProfile().getId();
        this.members.addAll(team.members().entrySet()
            .stream()
            .filter(member -> !member.getValue().status().isFakePlayer())
            .map(Map.Entry::getKey)
            .map(id -> Objects.requireNonNull(Minecraft.getInstance().getConnection()).getPlayerInfo(id))
            .map(Objects::requireNonNull)
            .toList()
        );
    }

    @Override
    protected void init() {
        //MouseLocationFix.fix(this.getClass());
        super.init();

        addRenderableWidget(new MembersList(this.leftPos + 8, this.topPos + 29, 70, 180, 20, this.members, entry -> {
            if (entry != null) {
                this.selectedProfile = entry.playerInfo().getProfile();
                this.selectedMember = this.team.members().get(this.selectedProfile.getId());
                rebuildWidgets();
            }
        }));

        if (this.selectedProfile != null && this.selectedMember != null) {
            SettingList list = addRenderableWidget(new SettingList(this.leftPos + 84, this.topPos + 29, 184, 180));
            addSettingsContent(list, this.selectedProfile, this.selectedMember);
        }
    }

    private void addSettingsContent(SettingList list, GameProfile profile, Member member) {
        list.addEntry(new TextEntry(ConstantComponents.MEMBER_STATUS, member.status().getDisplayName()));
        list.addEntry(new DividerEntry(ConstantComponents.MEMBER_PERMISSIONS));

        this.permissions.forEach(permission -> {
            Component title = Component.translatable("permission.argonauts." + permission);
            Component description = Component.translatable("permission.argonauts." + permission + ".description");

            list.addEntry(new BooleanEntry(
                    title,
                    description,
                    member.hasPermission(permission),
                    member.status().isMember() && team.canManagePermissions(this.selfId) && !team.isOwner(profile.getId()),
                    newValue -> ScreenUtils.sendCommand("%s permissions set %s %s %s".formatted(team.type(), permission, profile.getName(), newValue))
                )
            );
        });

        list.addEntry(new DividerEntry(ConstantComponents.MEMBER_ACTIONS));
        list.addEntry(new CommandEntry(
            ConstantComponents.REMOVE_MEMBER,
            ConstantComponents.REMOVE,
            "%s kick %s".formatted(team.type(), profile.getName()),
            team.canManageMembers(this.selfId) && !team.isOwner(profile.getId())
        ));
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, this.titleLabelX, this.titleLabelY, 0x404040, false);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        graphics.blit(CONTAINER_BACKGROUND, x, y, 0, 0, this.imageWidth, this.imageHeight, 512, 512);
    }

    @Override
    public void removed() {
        super.removed();
        //MouseLocationFix.setFix(clazz -> clazz == MembersScreen.class);
    }

    public static void openGuild() {
        GuildApi.API.getPlayerGuild(Minecraft.getInstance().player).ifPresent(guild ->
            Minecraft.getInstance().tell(() ->
                Minecraft.getInstance().setScreen(new MembersScreen(ConstantComponents.GUILD_MEMBERS_TITLE, guild, MemberPermissionsApi.API.getGuildPermissions().keySet()))
            )
        );
    }

    public static void openParty() {
        PartyApi.API.getPlayerParty(Minecraft.getInstance().player).ifPresent(party ->
            Minecraft.getInstance().tell(() ->
                Minecraft.getInstance().setScreen(new MembersScreen(ConstantComponents.PARTY_MEMBERS_TITLE, party, MemberPermissionsApi.API.getPartyPermissions().keySet()))
            )
        );
    }
}
