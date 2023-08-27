package earth.terrarium.argonauts.common.compat.cadmus;

import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.guild.Guild;
import earth.terrarium.cadmus.api.teams.TeamProviderApi;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class CadmusIntegration {
    public static final ResourceLocation ARGONAUTS_ID = new ResourceLocation(Argonauts.MOD_ID, Argonauts.MOD_ID);

    public static void init() {
        TeamProviderApi.API.register(ARGONAUTS_ID, new ArgonautsTeamProvider());
        TeamProviderApi.API.setSelected(ARGONAUTS_ID);
    }

    public static void addToCadmusTeam(ServerPlayer player, String id) {
        if (TeamProviderApi.API.getSelected() instanceof ArgonautsTeamProvider provider) {
            provider.onTeamChanged(player.server, id, player.getUUID());
        }
    }

    public static void removeFromCadmusTeam(ServerPlayer player, String id) {
        if (TeamProviderApi.API.getSelected() instanceof ArgonautsTeamProvider provider) {
            provider.onTeamChanged(player.server, id, player.getUUID());
        }
    }

    public static void disbandCadmusTeam(Guild guild, MinecraftServer server) {
        if (TeamProviderApi.API.getSelected() instanceof ArgonautsTeamProvider provider) {
            provider.onTeamRemoved(server, guild.id().toString());
        }
    }
}
