package earth.terrarium.odyssey_guilds.neoforge;

import earth.terrarium.odyssey_guilds.OdysseyGuilds;
import earth.terrarium.odyssey_guilds.common.commands.OdysseyGuildsCommands;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod(OdysseyGuilds.MOD_ID)
public class OdysseyGuildsNeo {

    public OdysseyGuildsNeo() {
        OdysseyGuilds.init();
        NeoForge.EVENT_BUS.addListener(OdysseyGuildsNeo::onPlayerLoggedIn);
        NeoForge.EVENT_BUS.addListener(OdysseyGuildsNeo::onPlayerLoggedOut);
        NeoForge.EVENT_BUS.addListener(OdysseyGuildsNeo::registerCommands);
    }

    private static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            OdysseyGuilds.onPlayerJoin(player);
        }
    }

    private static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            OdysseyGuilds.onPlayerLeave(player);
        }
    }

    private static void registerCommands(RegisterCommandsEvent event) {
        OdysseyGuildsCommands.register(event.getDispatcher());
    }
}