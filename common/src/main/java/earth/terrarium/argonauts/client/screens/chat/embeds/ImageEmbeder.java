package earth.terrarium.argonauts.client.screens.chat.embeds;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import earth.terrarium.argonauts.client.rendering.UrlTexture;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;

import java.net.URI;

public class ImageEmbeder implements Embeder {

    @Override
    public void handle(PoseStack stack, URI uri) {
        if (!validPath(uri.getPath())) return;
        String url = uri.toString();
        ResourceLocation texture = resolveImage(url);
        RenderSystem.setShaderTexture(0, texture);
        var info = UrlTexture.getInfo(url);
        var mouse = Minecraft.getInstance().mouseHandler;
        Window mainWindow = Minecraft.getInstance().getWindow();
        double mouseX = mouse.xpos() * (double) mainWindow.getGuiScaledWidth() / (double) mainWindow.getScreenWidth();
        double mouseY = mouse.ypos() * (double) mainWindow.getGuiScaledHeight() / (double) mainWindow.getScreenHeight();
        stack.pushPose();
        stack.translate(mouseX, mouseY, 200);
        Gui.blit(stack, 5, 5, 0, 0, info.displayWidth(), info.displayHeight(), info.displayWidth(), info.displayHeight());
        stack.popPose();
    }

    private static boolean validPath(String path) {
        if (path.endsWith(".png") || path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".gif")) {
            return true;
        }
        return !path.contains(".");
    }

    public static ResourceLocation resolveImage(String url) {
        ResourceLocation id = UrlTexture.getTextureId(url);
        TextureManager manager = Minecraft.getInstance().getTextureManager();
        AbstractTexture texture = manager.getTexture(id, MissingTextureAtlasSprite.getTexture());
        if (texture == MissingTextureAtlasSprite.getTexture()) {
            manager.register(id, new UrlTexture(url));
        }
        return id;
    }
}
