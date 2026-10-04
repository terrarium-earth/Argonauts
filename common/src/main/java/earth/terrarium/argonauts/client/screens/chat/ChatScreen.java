package earth.terrarium.argonauts.client.screens.chat;

import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.platform.InputConstants;
import com.teamresourceful.resourcefullib.client.components.selection.ListEntry;
import com.teamresourceful.resourcefullib.client.utils.ScreenUtils;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.teams.Team;
import earth.terrarium.argonauts.api.teams.guild.GuildApi;
import earth.terrarium.argonauts.api.teams.party.PartyApi;
import earth.terrarium.argonauts.client.screens.BaseScreen;
import earth.terrarium.argonauts.client.screens.chat.embeds.EmbedHandler;
import earth.terrarium.argonauts.client.screens.chat.messages.ChatMemberList;
import earth.terrarium.argonauts.client.screens.chat.messages.ChatMessagesList;
import earth.terrarium.argonauts.client.utils.ClientUtils;
import earth.terrarium.argonauts.common.chat.ChatHandler;
import earth.terrarium.argonauts.common.chat.ChatMessage;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ChatScreen extends BaseScreen {

    private static final ResourceLocation CONTAINER_BACKGROUND = Argonauts.id("textures/gui/chat.png");

    private final Team team;
    private final int maxUsers;
    private final List<String> usernames = new ArrayList<>();

    private ChatMessagesList messages;
    private EditBox box;

    @Nullable
    private String embedUrl;

    public ChatScreen(Component displayName, Team team) {
        super(displayName, 276, 166);
        this.team = team;

        this.maxUsers = team.realMembersCount();

        this.usernames.addAll(this.team.onlineMembers(Objects.requireNonNull(Minecraft.getInstance().level))
            .stream()
            .map(Player::getGameProfile)
            .map(GameProfile::getName)
            .toList());
    }

    @Override
    protected void init() {
        super.init();

        this.messages = addRenderableWidget(new ChatMessagesList(this.leftPos + 8, this.topPos + 18, ChatHandler.getChannel(this.team.id())));
        addRenderableWidget(new ChatMemberList(this.leftPos + 198, this.topPos + 18)).update(this.usernames);

        box = addRenderableWidget(new EditBox(this.font, this.leftPos + 9, this.topPos + 149, 183, 9, Component.nullToEmpty("")));
        box.setMaxLength(ChatMessage.MAX_MESSAGE_LENGTH);
        box.setBordered(false);
    }

    @Override
    public void renderBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.renderBackground(graphics, mouseX, mouseY, partialTick);
        if (this.embedUrl != null) {
            EmbedHandler.handle(graphics, embedUrl);
            this.embedUrl = null;
        }
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, title, this.titleLabelX, this.titleLabelY, 0x404040, false);
        int online = this.usernames.size();
        graphics.drawString(this.font, Component.translatable("gui.argonauts.online_members", online, this.maxUsers), 198, this.titleLabelY, 0x404040, false);
        String charCount = ClientUtils.getSmallNumber(ChatMessage.MAX_MESSAGE_LENGTH - box.getValue().length());
        graphics.drawString(this.font, charCount, 191 - this.font.width(charCount), 137, 0x404040, false);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        graphics.blit(CONTAINER_BACKGROUND, x, y, 0, 0, this.imageWidth, this.imageHeight, 512, 512);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.getFocused() instanceof EditBox editBox) {
            if (keyCode == InputConstants.KEY_RETURN && !editBox.getValue().isEmpty()) {
                ScreenUtils.sendCommand(this.team.type() + " chat " + editBox.getValue());
                editBox.setValue("");
                return true;
            }
        }
        if (Minecraft.getInstance().options.keyInventory.matches(keyCode, scanCode)) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public void addMessage(ChatMessage message) {
        int amount = ((int) this.messages.getScrollAmount());
        int childrenSize = this.messages.children().size() * 10;
        if ((amount == (childrenSize - 120) && childrenSize >= 120) || (amount == 0 && childrenSize < 120)) {
            ListEntry entry = this.messages.addMessage(message);
            this.messages.ensureVisible(entry);
        } else {
            this.messages.addMessage(message);
        }
    }

    public void setEmbedUrl(String url) {
        this.embedUrl = url;
    }

    public static void openGuild() {
        GuildApi.API.getPlayerGuild(Minecraft.getInstance().player).ifPresent(guild ->
            Minecraft.getInstance().tell(() ->
                Minecraft.getInstance().setScreen(new ChatScreen(ConstantComponents.GUILD_CHAT_TITLE, guild))
            )
        );
    }

    public static void openParty() {
        PartyApi.API.getPlayerParty(Minecraft.getInstance().player).ifPresent(party ->
            Minecraft.getInstance().tell(() ->
                Minecraft.getInstance().setScreen(new ChatScreen(ConstantComponents.PARTY_CHAT_TITLE, party))
            )
        );
    }
}
