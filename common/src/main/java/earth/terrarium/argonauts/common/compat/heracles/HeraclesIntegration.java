package earth.terrarium.argonauts.common.compat.heracles;

import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.heracles.api.teams.TeamProviders;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class HeraclesIntegration {
    public static final ResourceLocation ARGONAUTS_ID = new ResourceLocation(Argonauts.MOD_ID, Argonauts.MOD_ID);

    public static void init() {
        TeamProviders.register(ARGONAUTS_ID, new ArgonautsTeamProvider());
    }

    public static void updateHeraclesChanger(ServerPlayer player) {
        ArgonautsTeamProvider.changed(player.serverLevel(), player.getUUID());
    }
}
