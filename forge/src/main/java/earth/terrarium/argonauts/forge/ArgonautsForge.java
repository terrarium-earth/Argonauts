package earth.terrarium.argonauts.forge;

import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.ArgonautsClient;
import earth.terrarium.argonauts.common.commands.ModCommands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Argonauts.MOD_ID)
public class ArgonautsForge {
    public ArgonautsForge() {
        Argonauts.init();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(ArgonautsForge::onClientSetup);
        var bus = MinecraftForge.EVENT_BUS;
        bus.addListener(ArgonautsForge::onPlayerLoggedIn);
        bus.addListener(ArgonautsForge::onPlayerLoggedOut);
        bus.addListener(ArgonautsForge::registerCommands);
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