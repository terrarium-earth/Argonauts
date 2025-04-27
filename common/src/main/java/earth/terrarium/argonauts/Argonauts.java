package earth.terrarium.argonauts;

import com.teamresourceful.resourcefullib.common.utils.modinfo.ModInfoUtils;
import earth.terrarium.argonauts.api.teams.guild.GuildApi;
import earth.terrarium.argonauts.api.teams.party.PartyApi;
import earth.terrarium.argonauts.common.compat.cadmus.CadmusCompat;
import earth.terrarium.argonauts.common.compat.heracles.HeraclesCompat;
import earth.terrarium.argonauts.common.compat.prometheus.PrometheusCompat;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import earth.terrarium.argonauts.common.network.NetworkHandler;
import earth.terrarium.argonauts.common.network.packets.ClientboundSyncGuildsPacket;
import earth.terrarium.argonauts.common.network.packets.ClientboundSyncPartiesPacket;
import earth.terrarium.argonauts.common.permissions.Permissions;
import earth.terrarium.argonauts.common.settings.Settings;
import earth.terrarium.argonauts.common.utils.ArgonautsGameRules;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public class Argonauts {

    public static final String MOD_ID = "argonauts";

    public static final boolean IS_CADMUS_LOADED = ModInfoUtils.isModLoaded("cadmus");
    public static final boolean IS_PROMETHEUS_LOADED = ModInfoUtils.isModLoaded("prometheus");
    public static final boolean IS_HERACLES_LOADED = ModInfoUtils.isModLoaded("heracles");

    public static final int DEFAULT_MAX_GUILD_MEMBERS = 50;
    public static final int DEFAULT_MAX_PARTY_MEMBERS = 50;

    public static void init() {
        NetworkHandler.init();
        ArgonautsGameRules.init();
        Settings.init();
        Permissions.init();
        if (IS_CADMUS_LOADED) CadmusCompat.init();
        if (IS_PROMETHEUS_LOADED) PrometheusCompat.init();
        if (IS_HERACLES_LOADED) HeraclesCompat.init();
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