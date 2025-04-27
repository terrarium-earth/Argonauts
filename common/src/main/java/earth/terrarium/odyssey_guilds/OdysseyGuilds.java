package earth.terrarium.odyssey_guilds;

import com.teamresourceful.resourcefullib.common.utils.modinfo.ModInfoUtils;
import earth.terrarium.odyssey_guilds.api.teams.guild.GuildApi;
import earth.terrarium.odyssey_guilds.api.teams.party.PartyApi;
import earth.terrarium.odyssey_guilds.common.compat.cadmus.ClaimsCompat;
import earth.terrarium.odyssey_guilds.common.compat.heracles.HeraclesCompat;
import earth.terrarium.odyssey_guilds.common.compat.prometheus.PrometheusCompat;
import earth.terrarium.odyssey_guilds.common.constants.ConstantComponents;
import earth.terrarium.odyssey_guilds.common.network.NetworkHandler;
import earth.terrarium.odyssey_guilds.common.network.packets.ClientboundSyncGuildsPacket;
import earth.terrarium.odyssey_guilds.common.network.packets.ClientboundSyncPartiesPacket;
import earth.terrarium.odyssey_guilds.common.permissions.Permissions;
import earth.terrarium.odyssey_guilds.common.settings.Settings;
import earth.terrarium.odyssey_guilds.common.utils.OdysseyGuildsGameRules;
import earth.terrarium.odyssey_guilds.common.utils.ModUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class OdysseyGuilds {

    public static final String MOD_ID = "odyssey_guilds";

    public static final boolean IS_CLAIMS_LOADED = ModInfoUtils.isModLoaded("odyssey_claims");
    public static final boolean IS_ROLES_LOADED = ModInfoUtils.isModLoaded("odyssey_roles");
    public static final boolean IS_QUESTS_LOADED = ModInfoUtils.isModLoaded("odyssey_quests");

    public static final int DEFAULT_MAX_GUILD_MEMBERS = 50;
    public static final int DEFAULT_MAX_PARTY_MEMBERS = 50;

    public static void init() {
        NetworkHandler.init();
        OdysseyGuildsGameRules.init();
        Settings.init();
        Permissions.init();
        if (IS_CLAIMS_LOADED) ClaimsCompat.init();
        if (IS_ROLES_LOADED) PrometheusCompat.init();
        if (IS_QUESTS_LOADED) HeraclesCompat.init();
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void onPlayerJoin(ServerPlayer player) {
        if (NetworkHandler.CHANNEL.canSendToPlayer(player, ClientboundSyncGuildsPacket.TYPE)) {
            NetworkHandler.CHANNEL.sendToPlayer(new ClientboundSyncGuildsPacket(GuildApi.API.getAll(player.level())), player);
        }

        if (NetworkHandler.CHANNEL.canSendToPlayer(player, ClientboundSyncPartiesPacket.TYPE)) {
            NetworkHandler.CHANNEL.sendToPlayer(new ClientboundSyncPartiesPacket(PartyApi.API.getAll()), player);
        }

        GuildApi.API.getPlayerGuild(player).ifPresent(guild -> {
            String motd = Settings.MOTD.get(guild);
            if (!motd.isBlank()) {
                player.displayClientMessage(ConstantComponents.MOTD_HEADER, false);
                player.displayClientMessage(ModUtils.getParsedComponent(Component.literal(motd), player), false);
                player.displayClientMessage(ConstantComponents.MOTD_LINE, false);
            }
        });
    }

    public static void onPlayerLeave(ServerPlayer player) {
        PartyApi.API.getPlayerParty(player).ifPresent(party -> {
            if (party.isOwner(player.getUUID())) {
                PartyApi.API.disband(player.level(), party);
            }
        });
    }
}