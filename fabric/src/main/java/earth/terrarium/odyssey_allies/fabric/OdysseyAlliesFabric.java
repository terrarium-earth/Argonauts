package earth.terrarium.odyssey_allies.fabric;

import earth.terrarium.odyssey_allies.OdysseyAllies;
import earth.terrarium.odyssey_allies.common.commands.AlliesCommands;
import earth.terrarium.odyssey_allies.common.compat.placeholdersapi.AlliesPlaceholders;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;

public class OdysseyAlliesFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        OdysseyAllies.init();

        if (FabricLoader.getInstance().isModLoaded("placeholder-api")) {
            AlliesPlaceholders.init();
        }

        CommandRegistrationCallback.EVENT.register((dispatcher, context, selection) -> AlliesCommands.register(dispatcher));
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> OdysseyAllies.onPlayerJoin(handler.player));
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> OdysseyAllies.onPlayerLeave(handler.player));
    }
}