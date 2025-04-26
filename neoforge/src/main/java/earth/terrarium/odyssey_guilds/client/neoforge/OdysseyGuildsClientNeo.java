package earth.terrarium.odyssey_guilds.client.neoforge;

import earth.terrarium.odyssey_guilds.OdysseyGuilds;
import earth.terrarium.odyssey_guilds.api.teams.guild.GuildApi;
import earth.terrarium.odyssey_guilds.api.teams.party.PartyApi;
import earth.terrarium.odyssey_guilds.client.OdysseyGuildsClient;
import earth.terrarium.odyssey_guilds.client.screens.chat.ChatScreen;
import earth.terrarium.odyssey_guilds.client.screens.members.MembersScreen;
import earth.terrarium.odyssey_guilds.client.screens.settings.SettingsScreen;
import earth.terrarium.odyssey_guilds.common.commands.TeamExceptions;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.Commands;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

@Mod(value = OdysseyGuilds.MOD_ID, dist = Dist.CLIENT)
public class OdysseyGuildsClientNeo {

    public OdysseyGuildsClientNeo() {
        NeoForge.EVENT_BUS.addListener(OdysseyGuildsClientNeo::onClientTick);
        NeoForge.EVENT_BUS.addListener(OdysseyGuildsClientNeo::onPlayerLoggedOut);
        NeoForge.EVENT_BUS.addListener(OdysseyGuildsClientNeo::onRegisterClientCommands);
        OdysseyGuildsClient.init();
    }

    public static void onClientTick(ClientTickEvent.Pre event) {
        OdysseyGuildsClient.clientTick();
    }

    @SubscribeEvent
    public static void onRegisterKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(OdysseyGuildsClient.KEY_OPEN_PARTY_CHAT);
        event.register(OdysseyGuildsClient.KEY_OPEN_GUILD_CHAT);
    }

    private static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        OdysseyGuildsClient.onPlayerLoggedOut();
    }

    public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        event.getDispatcher().register((Commands.literal("guild").then(Commands.literal("chat").executes(context -> {
            if (GuildApi.API.getPlayerGuild(Minecraft.getInstance().player).isEmpty()) throw TeamExceptions.NOT_IN_GUILD.create();
            ChatScreen.openGuild();
            return 0;
        }))));

        event.getDispatcher().register((Commands.literal("party").then(Commands.literal("chat").executes(context -> {
            if (PartyApi.API.getPlayerParty(Minecraft.getInstance().player).isEmpty()) throw TeamExceptions.NOT_IN_PARTY.create();
            ChatScreen.openParty();
            return 0;
        }))));

        event.getDispatcher().register(Commands.literal("gc").executes(context -> {
            if (GuildApi.API.getPlayerGuild(Minecraft.getInstance().player).isEmpty()) throw TeamExceptions.NOT_IN_GUILD.create();
            ChatScreen.openGuild();
            return 0;
        }));

        event.getDispatcher().register((Commands.literal("guild").then(Commands.literal("members").executes(context -> {
            if (GuildApi.API.getPlayerGuild(Minecraft.getInstance().player).isEmpty()) throw TeamExceptions.NOT_IN_GUILD.create();
            MembersScreen.openGuild();
            return 0;
        }))));

        event.getDispatcher().register((Commands.literal("party").then(Commands.literal("members").executes(context -> {
            if (PartyApi.API.getPlayerParty(Minecraft.getInstance().player).isEmpty()) throw TeamExceptions.NOT_IN_PARTY.create();
            MembersScreen.openParty();
            return 0;
        }))));

        event.getDispatcher().register((Commands.literal("guild").then(Commands.literal("settings").executes(context -> {
            if (GuildApi.API.getPlayerGuild(Minecraft.getInstance().player).isEmpty()) throw TeamExceptions.NOT_IN_GUILD.create();
            SettingsScreen.openGuild();
            return 0;
        }))));

        event.getDispatcher().register((Commands.literal("party").then(Commands.literal("settings").executes(context -> {
            if (PartyApi.API.getPlayerParty(Minecraft.getInstance().player).isEmpty()) throw TeamExceptions.NOT_IN_PARTY.create();
            SettingsScreen.openParty();
            return 0;
        }))));
    }
}
