package earth.terrarium.odyssey_guilds.fabric;

import earth.terrarium.odyssey_guilds.OdysseyGuilds;
import earth.terrarium.odyssey_guilds.common.commands.OdysseyGuildsCommands;
import earth.terrarium.odyssey_guilds.common.compat.placeholdersapi.PlaceholdersCompat;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.loader.api.FabricLoader;

public class OdysseyGuildsFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        OdysseyGuilds.init();

        if (FabricLoader.getInstance().isModLoaded("placeholder-api")) {
            PlaceholdersCompat.init();
        }

        CommandRegistrationCallback.EVENT.register((dispatcher, context, selection) -> OdysseyGuildsCommands.register(dispatcher));
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> OdysseyGuilds.onPlayerJoin(handler.player));
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> OdysseyGuilds.onPlayerLeave(handler.player));
    }
}