package earth.terrarium.odyssey_allies.client.neoforge;

import earth.terrarium.odyssey_allies.OdysseyAllies;
import earth.terrarium.odyssey_allies.api.teams.guild.GuildApi;
import earth.terrarium.odyssey_allies.api.teams.party.PartyApi;
import earth.terrarium.odyssey_allies.client.OdysseyAlliesClient;
import earth.terrarium.odyssey_allies.client.screens.chat.ChatScreen;
import earth.terrarium.odyssey_allies.client.screens.members.MembersScreen;
import earth.terrarium.odyssey_allies.client.screens.settings.SettingsScreen;
import earth.terrarium.odyssey_allies.common.commands.AlliesExcepetions;
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

@Mod(value = OdysseyAllies.MOD_ID, dist = Dist.CLIENT)
public class OdysseyAlliesClientNeoForge {

    public OdysseyAlliesClientNeoForge() {
        NeoForge.EVENT_BUS.addListener(OdysseyAlliesClientNeoForge::onClientTick);
        NeoForge.EVENT_BUS.addListener(OdysseyAlliesClientNeoForge::onPlayerLoggedOut);
        NeoForge.EVENT_BUS.addListener(OdysseyAlliesClientNeoForge::onRegisterClientCommands);
        OdysseyAlliesClient.init();
    }

    public static void onClientTick(ClientTickEvent.Pre event) {
        OdysseyAlliesClient.clientTick();
    }

    @SubscribeEvent
    public static void onRegisterKeyBindings(RegisterKeyMappingsEvent event) {
        event.register(OdysseyAlliesClient.KEY_OPEN_PARTY_CHAT);
        event.register(OdysseyAlliesClient.KEY_OPEN_GUILD_CHAT);
    }

    private static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        OdysseyAlliesClient.onPlayerLoggedOut();
    }

    public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        event.getDispatcher().register((Commands.literal("guild").then(Commands.literal("chat").executes(context -> {
            if (GuildApi.API.getPlayerGuild(Minecraft.getInstance().player).isEmpty()) throw AlliesExcepetions.NOT_IN_GUILD.create();
            ChatScreen.openGuild();
            return 0;
        }))));

        event.getDispatcher().register((Commands.literal("party").then(Commands.literal("chat").executes(context -> {
            if (PartyApi.API.getPlayerParty(Minecraft.getInstance().player).isEmpty()) throw AlliesExcepetions.NOT_IN_PARTY.create();
            ChatScreen.openParty();
            return 0;
        }))));

        event.getDispatcher().register(Commands.literal("gc").executes(context -> {
            if (GuildApi.API.getPlayerGuild(Minecraft.getInstance().player).isEmpty()) throw AlliesExcepetions.NOT_IN_GUILD.create();
            ChatScreen.openGuild();
            return 0;
        }));

        event.getDispatcher().register((Commands.literal("guild").then(Commands.literal("members").executes(context -> {
            if (GuildApi.API.getPlayerGuild(Minecraft.getInstance().player).isEmpty()) throw AlliesExcepetions.NOT_IN_GUILD.create();
            MembersScreen.openGuild();
            return 0;
        }))));
        event.getDispatcher().register((Commands.literal("party").then(Commands.literal("members").executes(context -> {
            if (PartyApi.API.getPlayerParty(Minecraft.getInstance().player).isEmpty()) throw AlliesExcepetions.NOT_IN_PARTY.create();
            MembersScreen.openParty();
            return 0;
        }))));

        event.getDispatcher().register((Commands.literal("guild").then(Commands.literal("settings").executes(context -> {
            if (GuildApi.API.getPlayerGuild(Minecraft.getInstance().player).isEmpty()) throw AlliesExcepetions.NOT_IN_GUILD.create();
            SettingsScreen.openGuild();
            return 0;
        }))));
        event.getDispatcher().register((Commands.literal("party").then(Commands.literal("settings").executes(context -> {
            if (PartyApi.API.getPlayerParty(Minecraft.getInstance().player).isEmpty()) throw AlliesExcepetions.NOT_IN_PARTY.create();
            SettingsScreen.openParty();
            return 0;
        }))));
    }
}
