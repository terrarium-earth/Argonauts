package earth.terrarium.argonauts.client.widgets;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.RemotePlayer;
import net.minecraft.world.entity.player.PlayerModelPart;
import net.minecraft.world.item.component.ResolvableProfile;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PuppetPlayer extends RemotePlayer {

    private PlayerInfo playerInfo;

    public PuppetPlayer(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
        new ResolvableProfile(Optional.of(gameProfile.getName()), Optional.empty(), new PropertyMap()).resolve().thenAcceptAsync(resolvableProfile -> {
            this.playerInfo = new PlayerInfo(resolvableProfile.gameProfile(), false);
        }, Minecraft.getInstance());
    }

    @Override
    protected @Nullable PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    @Override
    public boolean isModelPartShown(PlayerModelPart part) {
        return true;
    }

    @Override
    public boolean shouldShowName() {
        return false;
    }
}
