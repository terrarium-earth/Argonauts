package earth.terrarium.argonauts;

import com.mojang.logging.LogUtils;
import com.teamresourceful.resourcefullib.common.utils.modinfo.ModInfoUtils;
import earth.terrarium.argonauts.api.guild.Guild;
import earth.terrarium.argonauts.api.guild.GuildApi;
import earth.terrarium.argonauts.api.party.Party;
import earth.terrarium.argonauts.api.party.PartyApi;
import earth.terrarium.argonauts.common.compat.cadmus.CadmusIntegration;
import earth.terrarium.argonauts.common.compat.heracles.HeraclesIntegration;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import earth.terrarium.argonauts.common.network.NetworkHandler;
import earth.terrarium.argonauts.common.network.messages.ClientboundSyncGuildsPacket;
import earth.terrarium.argonauts.common.network.messages.ClientboundSyncPartiesPacket;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Argonauts {
    public static final String MOD_ID = "argonauts";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static void init() {
        NetworkHandler.init();
        if (isCadmusLoaded()) {
            CadmusIntegration.init();
        }
        if (isHeraclesLoaded()) {
            HeraclesIntegration.init();
        }
    }

    public static void onPlayerJoin(Player player) {
        if (player.level().isClientSide()) return;
        NetworkHandler.CHANNEL.sendToPlayer(new ClientboundSyncGuildsPacket(new HashSet<>(GuildApi.API.getAll(player.getServer())), Set.of()), player);
        NetworkHandler.CHANNEL.sendToPlayer(new ClientboundSyncPartiesPacket(new HashSet<>(PartyApi.API.getAll()), Set.of()), player);
        Guild guild = GuildApi.API.get((ServerPlayer) player);
        if (guild == null) return;
        Component motd = guild.settings().motd();
        if (motd.getString().isEmpty()) return;

        player.displayClientMessage(ConstantComponents.MOTD_HEADER, false);
        player.displayClientMessage(ModUtils.getParsedComponent(motd, (ServerPlayer) player), false);
        player.displayClientMessage(ConstantComponents.LINE, false);
    }

    public static void onPlayerLeave(Player player) {
        if (player.level().isClientSide()) return;
        Party party = PartyApi.API.getPlayerParty(player.getUUID());
        if (party == null) return;
        if (!party.members().isLeader(player.getUUID())) return;

        PartyApi.API.disband(party, Objects.requireNonNull(player.getServer()));
    }

    public static boolean isCadmusLoaded() {
        return ModInfoUtils.isModLoaded("cadmus");
    }

    public static boolean isHeraclesLoaded() {
        return ModInfoUtils.isModLoaded("heracles");
    }
}