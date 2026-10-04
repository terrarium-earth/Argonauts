package earth.terrarium.argonauts.client.screens.chat.embeds;

import com.google.gson.JsonObject;
import com.mojang.blaze3d.platform.Window;
import com.teamresourceful.resourcefullib.client.CloseablePoseStack;
import com.teamresourceful.resourcefullib.client.utils.RenderUtils;
import com.teamresourceful.resourcefullib.common.utils.WebUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class YoutubeVideoEmbeder implements Embeder {

    private static final Map<String, YoutubeVideoInfo> INFO = Object2ObjectMaps.synchronize(new Object2ObjectOpenHashMap<>());

    @Override
    public void handle(GuiGraphics graphics, URI uri) {
        resolve(uri.toString());
        var info = INFO.get(uri.toString());
        if (info == null) return;
        String title = info.title;
        String author = info.author;
        String thumbnail = info.image;
        if (title == null || author == null || thumbnail == null || !info.future.isDone()) return;
        ResourceLocation texture = ImageEmbeder.resolveImage(thumbnail);
        var mouse = Minecraft.getInstance().mouseHandler;
        Window mainWindow = Minecraft.getInstance().getWindow();
        double mouseX = mouse.xpos() * (double) mainWindow.getGuiScaledWidth() / (double) mainWindow.getScreenWidth();
        double mouseY = mouse.ypos() * (double) mainWindow.getGuiScaledHeight() / (double) mainWindow.getScreenHeight();
        try (var pose = new CloseablePoseStack(graphics)) {
            pose.translate(mouseX, mouseY, 200);
            graphics.blit(texture, 5, 5, 0, 0, info.width, info.height, info.width, info.height);
            graphics.fill(5, 5 + info.height - 25, 5 + info.width, 5 + info.height, 0x80000000);
            try (var ignored = RenderUtils.createScissor(Minecraft.getInstance(), graphics, 5, 5 + info.height - 25, info.width, 25)) {
                graphics.drawString(
                    Minecraft.getInstance().font,
                    author, 8, 5 + info.height - 25 + 2, 0xFFFFFF,
                    false
                );
                var split = Minecraft.getInstance().font.split(Component.literal(title), info.width - 20);
                if (!split.isEmpty()) {
                    var draw = graphics.drawString(
                        Minecraft.getInstance().font,
                        split.get(0), 8, 5 + info.height - 25 + 13, 0x00A8EF,
                        false
                    );
                    if (split.size() > 1) {
                        graphics.drawString(
                            Minecraft.getInstance().font,
                            "...", draw, 5 + info.height - 25 + 13, 0x00A8EF,
                            false
                        );
                    }
                }
            }
        }
    }

    private static void resolve(String url) {
        if (INFO.containsKey(url)) return;
        INFO.put(url, new YoutubeVideoInfo(
            CompletableFuture.runAsync(() -> {
                try {
                    JsonObject object = WebUtils.getJson("https://www.youtube.com/oembed?url=" + url + "&format=json", true);
                    if (object == null) return;
                    YoutubeVideoInfo info = INFO.get(url);
                    if (info == null) return;
                    info.update(object);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
