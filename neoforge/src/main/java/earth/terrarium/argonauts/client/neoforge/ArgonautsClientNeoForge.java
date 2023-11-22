package earth.terrarium.argonauts.client.neoforge;

import earth.terrarium.argonauts.client.ArgonautsClient;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TickEvent;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ArgonautsClientNeoForge {
    public static void init() {
        NeoForge.EVENT_BUS.addListener(ArgonautsClientNeoForge::onClientTick);
    }

    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase.equals(TickEvent.Phase.START)) {
            ArgonautsClient.clientTick();
        }
    }

    @SubscribeEvent
    public static void onRegisterKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(ArgonautsClient.KEY_OPEN_PARTY_CHAT);
        event.register(ArgonautsClient.KEY_OPEN_GUILD_CHAT);
    }
}
