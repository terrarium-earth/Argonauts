package earth.terrarium.argonauts.client.screens.chat;

import com.google.common.primitives.UnsignedInteger;
import com.mojang.blaze3d.platform.InputConstants;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.base.BaseScreen;
import earth.terrarium.argonauts.client.screens.chat.embeds.EmbedHandler;
import earth.terrarium.argonauts.client.screens.chat.messages.ChatMemberList;
import earth.terrarium.argonauts.client.screens.chat.messages.ChatMessagesList;
import earth.terrarium.argonauts.client.utils.ClientUtils;
import earth.terrarium.argonauts.client.utils.MouseLocationFix;
import earth.terrarium.argonauts.common.handlers.chat.ChatMessage;
import earth.terrarium.argonauts.common.menus.ChatContent;
import earth.terrarium.argonauts.common.network.NetworkHandler;
import earth.terrarium.argonauts.common.network.messages.ServerboundChatWindowPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomChatScreen extends BaseScreen<ChatContent> {

    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(Argonauts.MOD_ID, "textures/gui/chat.png");

    private ChatMessagesList messages;
    private EditBox box;

    private String embedUrl = null;
    private final Logger logger = LoggerFactory.getLogger("Argonauts Chat");

    public CustomChatScreen(ChatContent menuContent, Component displayName) {
        super(menuContent, displayName, 276, 166);
    }

    @Override
    protected void init() {
        MouseLocationFix.fix(this.getClass());
        super.init();

        this.messages = addRenderableWidget(new ChatMessagesList(this.leftPos + 8, this.topPos + 18));
        this.messages.update(this.menuContent.messages());
        addRenderableWidget(new ChatMemberList(this.leftPos + 198, this.topPos + 18)).update(this.menuContent.usernames());

        box = addRenderableWidget(new EditBox(this.font, this.leftPos + 9, this.topPos + 149, 183, 9, Component.nullToEmpty("")));
        box.setMaxLength(ChatMessage.MAX_MESSAGE_LENGTH);
        box.setBordered(false);
    }

    public void addMessage(UnsignedInteger id, ChatMessage message) {
        int amount = ((int) this.messages.getScrollAmount());
        int childrenSize = this.messages.children().size() * 10;
        if ((amount == (childrenSize - 120) && childrenSize >= 120) || (amount == 0 && childrenSize < 120)) {
            var entry = this.messages.addMessage(id, message);
            this.messages.ensureVisible(entry);
        } else {
            this.messages.addMessage(id, message);
        }
        if (!Minecraft.getInstance().isLocalServer()) {
            logger.info(String.format("[%s] <%s> %s", this.menuContent.type().name(), message.profile().getName(), message.message()));
        }
    }

    public void deleteMessage(UnsignedInteger id) {
        this.messages.deleteMessage(id);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int i, int j, float f) {
        this.renderBackground(graphics);
        super.render(graphics, i, j, f);

        if (this.embedUrl != null) {
            EmbedHandler.handle(graphics, embedUrl);
            this.embedUrl = null;
        }
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(this.font, "Chat", this.titleLabelX, this.titleLabelY, 0x404040, false);
        int online = this.menuContent.usernames().size();
        graphics.drawString(this.font, online + "/" + this.menuContent.maxUsers() + " Online", 198, this.titleLabelY, 0x404040, false);
        String charCount = ClientUtils.getSmallNumber(ChatMessage.MAX_MESSAGE_LENGTH - box.getValue().length());
        graphics.drawString(this.font, charCount, 191 - this.font.width(charCount), 137, 0x404040, false);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        graphics.blit(CONTAINER_BACKGROUND, k, l, 0, 0, this.imageWidth, this.imageHeight, 512, 512);
    }

    @Override
    public void removed() {
        super.removed();
        MouseLocationFix.setFix(clas -> clas == CustomChatScreen.class);
    }

    @Override
    public boolean keyPressed(int i, int j, int k) {
        if (this.getFocused() instanceof EditBox editBox) {
            if (i == InputConstants.KEY_RETURN && !editBox.getValue().isEmpty()) {
                NetworkHandler.CHANNEL.sendToServer(new ServerboundChatWindowPacket(editBox.getValue(), this.menuContent.type()));
                editBox.setValue("");
                return true;
            }
        }
        if (Minecraft.getInstance().options.keyInventory.matches(i, j)) {
            return true;
        }
        return super.keyPressed(i, j, k);
    }

    public void setEmbedUrl(String url) {
        this.embedUrl = url;
    }

    public static void open(ChatContent menuContent, Component displayName) {
        Minecraft.getInstance().setScreen(new CustomChatScreen(menuContent, displayName));
    }
}
