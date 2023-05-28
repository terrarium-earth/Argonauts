package earth.terrarium.argonauts.client.screens.chat.embeds;

import net.minecraft.client.gui.GuiGraphics;

import java.net.URI;

public interface Embeder {

    void handle(GuiGraphics graphics, URI uri);
}
