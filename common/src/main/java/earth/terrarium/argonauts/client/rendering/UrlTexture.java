package earth.terrarium.argonauts.client.rendering;

import com.google.common.hash.Hashing;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.platform.TextureUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.logging.LogUtils;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.utils.ClientUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectMaps;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class UrlTexture extends SimpleTexture {

    private static final Map<String, UrlTexture.Info> INFO = Object2ObjectMaps.synchronize(new Object2ObjectOpenHashMap<>());
    private static final Info DEFAULT_INFO = new Info(24, 24, 24, 24);

    private static final Logger LOGGER = LogUtils.getLogger();
    private static final ResourceLocation DEFAULT_TEXTURE = new ResourceLocation(Argonauts.MOD_ID, "textures/gui/hourglass.png");

    private final HttpRequest request;
    private final String url;
    private boolean loaded;
    private CompletableFuture<?> loader;

    public UrlTexture(String url) {
        super(DEFAULT_TEXTURE);
        this.url = url;
        this.request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "Argonauts (Minecraft Mod)")
                .build();
    }

    @SuppressWarnings({"deprecation"})
    public static ResourceLocation getTextureId(String url) {
        return new ResourceLocation(Argonauts.MOD_ID, "urlimages/" + Hashing.sha1().hashUnencodedChars(url));
    }

    public static Info getInfo(String url) {
        return INFO.getOrDefault(url, DEFAULT_INFO);
    }

    private void upload(NativeImage image) {
        TextureUtil.prepareImage(this.getId(), image.getWidth(), image.getHeight());
        image.upload(0, 0, 0, true);
        INFO.put(this.url, Info.from(image));
    }

    @Override
    public void load(@NotNull ResourceManager manager) {
        Minecraft.getInstance().execute(() -> {
            if (!this.loaded) {
                try {
                    super.load(manager);
                } catch (IOException ignored) {}
                this.loaded = true;
            }
        });

        if (this.loader == null) {
            this.loader = CompletableFuture.runAsync(() -> {
                try {
                    HttpResponse<InputStream> data = ClientUtils.WEB.send(request, HttpResponse.BodyHandlers.ofInputStream());
                    if (data.statusCode() / 100 == 2) {
                        NativeImage image = this.loadTexture(data.body());
                        Minecraft.getInstance().execute(() -> {
                            if (image != null) {
                                Minecraft.getInstance().execute(() -> {
                                    this.loaded = true;
                                    if (!RenderSystem.isOnRenderThread()) {
                                        RenderSystem.recordRenderCall(() -> this.upload(image));
                                    } else {
                                        this.upload(image);
                                    }
                                });
                            }

                        });
                    }
                } catch (IOException | InterruptedException ignored) {}
            }, Util.backgroundExecutor());
        }
    }

    @Nullable
    private NativeImage loadTexture(InputStream stream) {
        NativeImage nativeImage = null;

        try {
            nativeImage = NativeImage.read(stream);
        } catch (Exception ignored) {}

        return nativeImage;
    }

    public record Info(int width, int height, int displayWidth, int displayHeight) {

        public static Info from(NativeImage image) {
            double widthMultiple = Math.ceil((double) image.getWidth() / (double) 150);
            double heightMultiple = Math.ceil((double) image.getHeight() / (double) 100);
            int ratio = (int) Math.max(widthMultiple, heightMultiple);

            return new Info(image.getWidth(), image.getHeight(), image.getWidth() / ratio, image.getHeight() / ratio);
        }
    }
}