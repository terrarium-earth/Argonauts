package earth.terrarium.argonauts.fabric;

import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.common.commands.ModCommands;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;

public class ArgonautsFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Argonauts.init();
        CommandRegistrationCallback.EVENT.register(ModCommands::register);
    }
}