package earth.terrarium.argonauts.neoforge;

import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.ArgonautsClient;
import earth.terrarium.argonauts.common.commands.ModCommands;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod(Argonauts.MOD_ID)
public class ArgonautsNeoForge {
    public ArgonautsNeoForge() {
        Argonauts.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ArgonautsNeoForge::onClientSetup);
        var bus = NeoForge.EVENT_BUS;
        bus.addListener(ArgonautsNeoForge::onPlayerLoggedIn);
        bus.addListener(ArgonautsNeoForge::onPlayerLoggedOut);
        bus.addListener(ArgonautsNeoForge::registerCommands);
    }

    private static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Argonauts.onPlayerJoin(player);
        }
    }

    private static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Argonauts.onPlayerLeave(player);
        }
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        ArgonautsClient.init();
    }

    private static void registerCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }
}