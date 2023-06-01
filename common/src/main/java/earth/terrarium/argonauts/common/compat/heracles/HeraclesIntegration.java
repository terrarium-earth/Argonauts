package earth.terrarium.argonauts.common.compat.heracles;

import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.heracles.api.teams.TeamProviders;
import net.minecraft.resources.ResourceLocation;

public class HeraclesIntegration {
    public static final ResourceLocation ARGONAUTS_ID = new ResourceLocation(Argonauts.MOD_ID, Argonauts.MOD_ID);

    public static void init() {
        TeamProviders.register(ARGONAUTS_ID, new ArgonautsTeamProvider());
    }
}
