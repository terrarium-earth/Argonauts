package earth.terrarium.argonauts.client.screens.chat;

import com.google.common.primitives.UnsignedInteger;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.screens.AbstractContainerCursorScreen;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.screens.chat.embeds.EmbedHandler;
import earth.terrarium.argonauts.client.screens.chat.messages.ChatMemberList;
import earth.terrarium.argonauts.client.screens.chat.messages.ChatMessagesList;
import earth.terrarium.argonauts.client.utils.ClientUtils;
import earth.terrarium.argonauts.client.utils.MouseLocationFix;
import earth.terrarium.argonauts.common.handlers.chat.ChatMessage;
import earth.terrarium.argonauts.common.menus.ChatMenu;
import earth.terrarium.argonauts.common.network.NetworkHandler;
import earth.terrarium.argonauts.common.network.messages.ServerboundChatWindowPacket;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class CustomChatScreen extends AbstractContainerCursorScreen<ChatMenu> {

    private static final ResourceLocation CONTAINER_BACKGROUND = new ResourceLocation(Argonauts.MOD_ID, "textures/gui/chat.png");

    private ChatMessagesList messages;
    private EditBox box;

    private String embedUrl = null;

    public CustomChatScreen(ChatMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        this.passEvents = false;
        this.imageHeight = 166;
        this.imageWidth = 276;
    }

    @Override
    protected void init() {
        MouseLocationFix.fix(this.getClass());
        super.init();

        this.messages = addRenderableWidget(new ChatMessagesList(this.leftPos + 8, this.topPos + 18));
        this.messages.update(this.menu.messages());
        addRenderableWidget(new ChatMemberList(this.leftPos + 198, this.topPos + 18)).update(this.menu.usernames());

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
    }

    public void deleteMessage(UnsignedInteger id) {
        this.messages.deleteMessage(id);
    }

    @Override
    public void render(@NotNull PoseStack stack, int i, int j, float f) {
        this.renderBackground(stack);
        super.render(stack, i, j, f);
        this.renderTooltip(stack, i, j);

        if (this.embedUrl != null) {
            EmbedHandler.handle(stack, embedUrl);
            this.embedUrl = null;
        }
    }

    @Override
    protected void renderLabels(@NotNull PoseStack stack, int i, int j) {
        this.font.draw(stack, title, (float) this.titleLabelX, (float) this.titleLabelY, 4210752);
        int online = this.menu.usernames().size();
        this.font.draw(stack, online + "/" + this.menu.maxUsers() + " Online", 198, (float) this.titleLabelY, 4210752);
        String charcount = ClientUtils.getSmallNumber(ChatMessage.MAX_MESSAGE_LENGTH - box.getValue().length());
        this.font.draw(stack, charcount, 191 - this.font.width(charcount), 137, 4210752);
    }

    @Override
    protected void renderBg(@NotNull PoseStack stack, float f, int i, int j) {
        RenderSystem.setShaderTexture(0, CONTAINER_BACKGROUND);
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        blit(stack, k, l, 0, 0, this.imageWidth, this.imageHeight, 512, 512);
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
                NetworkHandler.CHANNEL.sendToServer(new ServerboundChatWindowPacket(editBox.getValue()));
                editBox.setValue("");
                return true;
            }
        }
        if (this.minecraft.options.keyInventory.matches(i, j)) {
            return true;
        }
        return super.keyPressed(i, j, k);
    }

    public void setEmbedUrl(String url) {
        this.embedUrl = url;
    }
}
