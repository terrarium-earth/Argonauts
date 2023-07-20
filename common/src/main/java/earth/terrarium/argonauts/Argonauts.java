package earth.terrarium.argonauts;

import com.teamresourceful.resourcefullib.common.utils.modinfo.ModInfoUtils;
import earth.terrarium.argonauts.api.guild.Guild;
import earth.terrarium.argonauts.api.guild.GuildApi;
import earth.terrarium.argonauts.api.party.Party;
import earth.terrarium.argonauts.api.party.PartyApi;
import earth.terrarium.argonauts.common.compat.cadmus.CadmusIntegration;
import earth.terrarium.argonauts.common.compat.heracles.HeraclesIntegration;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import earth.terrarium.argonauts.common.network.NetworkHandler;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public class Argonauts {
    public static final String MOD_ID = "argonauts";

    public static void init() {
        NetworkHandler.init();
        if (isCadmusLoaded()) {
            CadmusIntegration.init();
        }
        if (isHeraclesLoaded()) {
            HeraclesIntegration.init();
        }
    }

    // Message of the day
    public static void onPlayerJoin(Player player) {
        if (player.level().isClientSide()) return;
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