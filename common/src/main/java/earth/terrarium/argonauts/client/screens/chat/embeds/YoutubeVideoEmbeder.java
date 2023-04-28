package earth.terrarium.argonauts.client.screens.chat.embeds;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.teamresourceful.resourcefullib.client.utils.RenderUtils;
import com.teamresourceful.resourcefullib.common.lib.Constants;
import earth.terrarium.argonauts.client.utils.ClientUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class YoutubeVideoEmbeder implements Embeder {

    private static final Map<String, YoutubeVideoInfo> INFO = Object2ObjectMaps.synchronize(new Object2ObjectOpenHashMap<>());

    @Override
    public void handle(PoseStack stack, URI uri) {
        resolve(uri.toString());
        var info = INFO.get(uri.toString());
        if (info == null) return;
        String title = info.title;
        String author = info.author;
        String thumbnail = info.image;
        if (title == null || author == null || thumbnail == null|| !info.future.isDone()) return;
        ResourceLocation texture = ImageEmbeder.resolveImage(thumbnail);
        RenderSystem.setShaderTexture(0, texture);
        var mouse = Minecraft.getInstance().mouseHandler;
        Window mainWindow = Minecraft.getInstance().getWindow();
        double mouseX = mouse.xpos() * (double) mainWindow.getGuiScaledWidth() / (double) mainWindow.getScreenWidth();
        double mouseY = mouse.ypos() * (double) mainWindow.getGuiScaledHeight() / (double) mainWindow.getScreenHeight();
        stack.pushPose();
        stack.translate(mouseX, mouseY, 200);
        Gui.blit(stack, 5, 5, 0, 0, info.width, info.height, info.width, info.height);
        Gui.fill(stack, 5, 5 + info.height - 25, 5 + info.width, 5 + info.height, 0x80000000);
        try (var ignored = RenderUtils.createScissorBox(Minecraft.getInstance(), stack, 5, 5 + info.height - 25,  info.width, 25)) {
            Minecraft.getInstance().font.draw(stack, author, 8, 5 + info.height - 25 + 2, 0xFFFFFF);
            var split = Minecraft.getInstance().font.split(Component.literal(title), info.width - 20);
            if (!split.isEmpty()) {
                var draw = Minecraft.getInstance().font.draw(stack, split.get(0), 8, 5 + info.height - 25 + 13, 0x00A8EF);
                if (split.size() > 1) {
                    Minecraft.getInstance().font.draw(stack, "...", draw, 5 + info.height - 25 + 13, 0x00A8EF);
                }
            }
        }
        stack.popPose();
    }

    private static void resolve(String url) {
        if (INFO.containsKey(url)) return;
        INFO.put(url, new YoutubeVideoInfo(
            CompletableFuture.runAsync(() -> {
                try {
                    HttpResponse<String> send = ClientUtils.WEB.send(HttpRequest.newBuilder()
                        .uri(URI.create("https://www.youtube.com/oembed?url=" + url + "&format=json"))
                        .header("User-Agent", "Argonauts (Minecraft Mod)")
                        .build(), HttpResponse.BodyHandlers.ofString());
                    if (send.statusCode() == 200) {
                        JsonObject object = Constants.GSON.fromJson(send.body(), JsonObject.class);
                        YoutubeVideoInfo info = INFO.get(url);
                        if (info == null) return;
                        INFO.get(url).update(object);
                    }
                } catch (Exception ignored) {}
            }, Util.backgroundExecutor())
        ));
    }

    private static class YoutubeVideoInfo {
        private final CompletableFuture<?> future;
        private String image;
        private String title;
        private String author;
        private int height;
        private int width;

        public YoutubeVideoInfo(CompletableFuture<?> future) {
            this.future = future;
        }

        public void update(JsonObject object) {
            this.title = object.get("title").getAsString();
            this.author = object.get("author_name").getAsString();
            this.image = object.get("thumbnail_url").getAsString();
            this.height = object.get("thumbnail_height").getAsInt();
            this.width = object.get("thumbnail_width").getAsInt();
            if (this.height > 200 || this.width > 250) {
                this.height *= 0.5f;
                this.width *= 0.5f;
            }
        }
    }
}
