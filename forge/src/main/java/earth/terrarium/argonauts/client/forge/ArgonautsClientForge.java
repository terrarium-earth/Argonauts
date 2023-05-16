package earth.terrarium.argonauts.client.forge;

import earth.terrarium.argonauts.client.ArgonautsClient;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ArgonautsClientForge {
    public static void init() {
        MinecraftForge.EVENT_BUS.addListener(ArgonautsClientForge::onClientTick);

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(ArgonautsClientForge::onRegisterKeyBindings);
    }

    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.START)) {
            ArgonautsClient.clientTick();
        }
    }

    public static void onRegisterKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(ArgonautsClient.KEY_OPEN_PARTY_CHAT);
        event.register(ArgonautsClient.KEY_OPEN_GUILD_CHAT);
    }
}
