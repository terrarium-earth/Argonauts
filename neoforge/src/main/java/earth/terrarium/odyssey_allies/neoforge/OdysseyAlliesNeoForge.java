package earth.terrarium.odyssey_allies.neoforge;

import earth.terrarium.odyssey_allies.OdysseyAllies;
import earth.terrarium.odyssey_allies.common.commands.AlliesCommands;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod(OdysseyAllies.MOD_ID)
public class OdysseyAlliesNeoForge {

    public OdysseyAlliesNeoForge() {
        OdysseyAllies.init();
        NeoForge.EVENT_BUS.addListener(OdysseyAlliesNeoForge::onPlayerLoggedIn);
        NeoForge.EVENT_BUS.addListener(OdysseyAlliesNeoForge::onPlayerLoggedOut);
        NeoForge.EVENT_BUS.addListener(OdysseyAlliesNeoForge::registerCommands);
    }

    private static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            OdysseyAllies.onPlayerJoin(player);
        }
    }

    private static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            OdysseyAllies.onPlayerLeave(player);
        }
    }

    private static void registerCommands(RegisterCommandsEvent event) {
        AlliesCommands.register(event.getDispatcher());
    }
}