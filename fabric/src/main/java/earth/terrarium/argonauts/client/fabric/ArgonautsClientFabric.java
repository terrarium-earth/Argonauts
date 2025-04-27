package earth.terrarium.argonauts.client.fabric;

import earth.terrarium.argonauts.api.teams.guild.GuildApi;
import earth.terrarium.argonauts.api.teams.party.PartyApi;
import earth.terrarium.argonauts.client.ArgonautsClient;
import earth.terrarium.argonauts.client.screens.chat.ChatScreen;
import earth.terrarium.argonauts.client.screens.members.MembersScreen;
import earth.terrarium.argonauts.client.screens.settings.SettingsScreen;
import earth.terrarium.argonauts.common.commands.TeamExceptions;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class ArgonautsClientFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ArgonautsClient.init();
        ClientTickEvents.START_CLIENT_TICK.register(client -> ArgonautsClient.clientTick());
        ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> ArgonautsClient.onPlayerLoggedOut());
        KeyBindingHelper.registerKeyBinding(ArgonautsClient.KEY_OPEN_PARTY_CHAT);
        KeyBindingHelper.registerKeyBinding(ArgonautsClient.KEY_OPEN_GUILD_CHAT);
        registerClientCommands();
    }

    // Fabric is really dumb and doesn't merge commands, so the head node needs to have a different name than the server so the `c` was added.
    // https://github.com/FabricMC/fabric/issues/1721
    private static void registerClientCommands() {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("guildc").then(ClientCommandManager.literal("chat").executes(context -> {
                if (GuildApi.API.getPlayerGuild(context.getSource().getPlayer()).isEmpty()) throw TeamExceptions.NOT_IN_GUILD.create();
                ChatScreen.openGuild();
                return 0;
            })));
            dispatcher.register(ClientCommandManager.literal("partyc").then(ClientCommandManager.literal("chat").executes(context -> {
                if (PartyApi.API.getPlayerParty(context.getSource().getPlayer()).isEmpty()) throw TeamExceptions.NOT_IN_PARTY.create();
                ChatScreen.openParty();
                return 0;
            })));
            dispatcher.register(ClientCommandManager.literal("gc").executes(context -> {
                if (GuildApi.API.getPlayerGuild(context.getSource().getPlayer()).isEmpty()) throw TeamExceptions.NOT_IN_GUILD.create();
                ChatScreen.openGuild();
                return 0;
            }));

            dispatcher.register((ClientCommandManager.literal("guildc").then(ClientCommandManager.literal("members").executes(context -> {
                if (GuildApi.API.getPlayerGuild(context.getSource().getPlayer()).isEmpty()) throw TeamExceptions.NOT_IN_GUILD.create();
                MembersScreen.openGuild();
                return 0;
            }))));
            dispatcher.register((ClientCommandManager.literal("partyc").then(ClientCommandManager.literal("members").executes(context -> {
                if (PartyApi.API.getPlayerParty(context.getSource().getPlayer()).isEmpty()) throw TeamExceptions.NOT_IN_PARTY.create();
                MembersScreen.openParty();
                return 0;
            }))));

            dispatcher.register((ClientCommandManager.literal("guildc").then(ClientCommandManager.literal("settings").executes(context -> {
                if (GuildApi.API.getPlayerGuild(context.getSource().getPlayer()).isEmpty()) throw TeamExceptions.NOT_IN_GUILD.create();
                SettingsScreen.openGuild();
                return 0;
            }))));
            dispatcher.register((ClientCommandManager.literal("partyc").then(ClientCommandManager.literal("settings").executes(context -> {
                if (PartyApi.API.getPlayerParty(context.getSource().getPlayer()).isEmpty()) throw TeamExceptions.NOT_IN_PARTY.create();
                SettingsScreen.openParty();
                return 0;
            }))));
        });
    }
}
