package earth.terrarium.argonauts.client.screens.chat.embeds;

import net.minecraft.client.gui.GuiGraphics;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

public class EmbedHandler {

    private static final Map<String, Embeder> HANDLERS = new HashMap<>();

    static {
        register("i.imgur.com", new ImageEmbeder());
        register("media.tenor.com", new ImageEmbeder());
        register("media.discordapp.net", new ImageEmbeder());
        register("cdn.discordapp.com", new ImageEmbeder());
        register("youtube.com", new YoutubeVideoEmbeder());
        register("www.youtube.com", new YoutubeVideoEmbeder());
        register("youtu.be", new YoutubeVideoEmbeder());
        register("www.youtu.be", new YoutubeVideoEmbeder());
        register("open.spotify.com", new SpotifyTrackEmbeder());
    }

    public static void register(String domain, Embeder handler) {
        HANDLERS.put(domain, handler);
    }

    public static void handle(GuiGraphics graphics, String url) {
        try {
            var uri = URI.create(url);
            var handler = HANDLERS.get(uri.getHost());
            if (handler != null) {
                handler.handle(graphics, uri);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
