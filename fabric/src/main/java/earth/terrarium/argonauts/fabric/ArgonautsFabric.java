package earth.terrarium.argonauts.fabric;

import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.common.commands.ModCommands;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class ArgonautsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Argonauts.init();
        CommandRegistrationCallback.EVENT.register(ModCommands::register);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> Argonauts.onPlayerJoin(handler.player));
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> Argonauts.onPlayerLeave(handler.player));
        ServerLifecycleEvents.SERVER_STARTED.register(Argonauts::serverStarted);
    }
}