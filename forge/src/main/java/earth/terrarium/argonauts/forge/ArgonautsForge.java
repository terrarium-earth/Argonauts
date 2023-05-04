package earth.terrarium.argonauts.forge;

import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.client.ArgonautsClient;
import earth.terrarium.argonauts.common.commands.ModCommands;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Argonauts.MOD_ID)
public class ArgonautsForge {
    public ArgonautsForge() {
        Argonauts.init();
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(ArgonautsForge::onClientSetup);

        MinecraftForge.EVENT_BUS.addListener(ArgonautsForge::registerCommands);
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        ArgonautsClient.init();
    }

    private static void registerCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher(), event.getBuildContext(), event.getCommandSelection());
    }
}