package earth.terrarium.argonauts.common.compat.cadmus;

import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.common.handlers.guild.Guild;
import earth.terrarium.cadmus.api.teams.TeamProviderApi;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class CadmusIntegration {
    public static final ResourceLocation ARGONAUTS_ID = new ResourceLocation(Argonauts.MOD_ID, Argonauts.MOD_ID);

    public static void init() {
        if (!(TeamProviderApi.API.getSelected() instanceof ArgonautsTeamProvider)) { // TODO remove
            TeamProviderApi.API.register(ARGONAUTS_ID, new ArgonautsTeamProvider());
            TeamProviderApi.API.setSelected(ARGONAUTS_ID);
        }
    }

    public static void addToCadmusTeam(Guild guild, ServerPlayer player) {
        if (TeamProviderApi.API.getSelected() instanceof ArgonautsTeamProvider provider) {
            provider.addPlayerToTeam(player.server, player.getUUID(), guild);
        }
    }

    public static void removeFromCadmusTeam(Guild guild, ServerPlayer player) {
        if (TeamProviderApi.API.getSelected() instanceof ArgonautsTeamProvider provider) {
            provider.removePlayerFromTeam(player.server, player.getUUID(), guild);
        }
    }

    public static void disbandCadmusTeam(Guild guild, ServerPlayer player) {
        if (TeamProviderApi.API.getSelected() instanceof ArgonautsTeamProvider provider) {
            provider.disbandTeam(player.server, player.getUUID(), guild);
        }
    }
}
