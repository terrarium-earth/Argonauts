package earth.terrarium.argonauts.common.compat.heracles;

import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.heracles.api.teams.TeamProviders;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class HeraclesIntegration {
    public static final ResourceLocation ARGONAUTS_ID = new ResourceLocation(Argonauts.MOD_ID, Argonauts.MOD_ID);

    public static void init() {
        TeamProviders.register(ARGONAUTS_ID, new ArgonautsTeamProvider());
    }

    public static void updateHeraclesChanger(ServerPlayer player) {
        updateHeraclesChanger(player.serverLevel(), player.getUUID());
    }

    public static void updateHeraclesChanger(ServerLevel level, UUID player) {
        ArgonautsTeamProvider.changed(level, player);
    }
}
