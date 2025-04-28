package earth.terrarium.odyssey_allies.client;

import com.mojang.blaze3d.platform.InputConstants;
import earth.terrarium.odyssey_allies.OdysseyAllies;
import earth.terrarium.odyssey_allies.client.compat.prometheus.PrometheusClientCompat;
import earth.terrarium.odyssey_allies.client.screens.chat.ChatScreen;
import earth.terrarium.odyssey_allies.common.chat.ChatHandler;
import earth.terrarium.odyssey_allies.common.constants.ConstantComponents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class OdysseyAlliesClient {

    public static final KeyMapping KEY_OPEN_PARTY_CHAT = new KeyMapping(
        ConstantComponents.KEY_OPEN_PARTY_CHAT.getString(),
        InputConstants.UNKNOWN.getValue(),
        ConstantComponents.ODYSSEY_CATEGORY.getString());
    public static final KeyMapping KEY_OPEN_GUILD_CHAT = new KeyMapping(
        ConstantComponents.KEY_OPEN_GUILD_CHAT.getString(),
        InputConstants.UNKNOWN.getValue(),
        ConstantComponents.ODYSSEY_CATEGORY.getString());

    public static void init() {
        if (OdysseyAllies.IS_ROLES_LOADED) {
            PrometheusClientCompat.init();
        }
    }

    public static void clientTick() {
        if (KEY_OPEN_PARTY_CHAT.consumeClick()) ChatScreen.openParty();
        if (KEY_OPEN_GUILD_CHAT.consumeClick()) ChatScreen.openGuild();
    }

    @NotNull
    public static Level level() {
        return Objects.requireNonNull(Minecraft.getInstance().level);
    }

    public static void onPlayerLoggedOut() {
        ChatHandler.clearChannels();
    }
}
