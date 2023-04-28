package earth.terrarium.argonauts.client.screens.chat.embeds;

import com.mojang.blaze3d.vertex.PoseStack;

import java.net.URI;

public interface Embeder {

    void handle(PoseStack stack, URI uri);
}
