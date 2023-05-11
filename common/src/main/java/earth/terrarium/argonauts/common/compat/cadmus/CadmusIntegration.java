package earth.terrarium.argonauts.common.compat.cadmus;

import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.common.handlers.guild.Guild;
import earth.terrarium.cadmus.api.teams.TeamProviderApi;
import earth.terrarium.cadmus.common.teams.TeamSaveData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class CadmusIntegration {
    public static final ResourceLocation ARGONAUTS_ID = new ResourceLocation(Argonauts.MOD_ID, Argonauts.MOD_ID);

    public static void init() {
        TeamProviderApi.API.register(ARGONAUTS_ID, new ArgonautsTeamProvider());
    }

    public static void setTeamProvider(MinecraftServer server) {
        TeamSaveData.read(server);
        if (TeamProviderApi.API.getSelectedId() == null) {
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
