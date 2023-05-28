package earth.terrarium.argonauts;

import com.teamresourceful.resourcefullib.common.utils.modinfo.ModInfoUtils;
import earth.terrarium.argonauts.common.compat.cadmus.CadmusIntegration;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import earth.terrarium.argonauts.common.handlers.guild.Guild;
import earth.terrarium.argonauts.common.handlers.guild.GuildHandler;
import earth.terrarium.argonauts.common.handlers.party.Party;
import earth.terrarium.argonauts.common.handlers.party.PartyHandler;
import earth.terrarium.argonauts.common.network.NetworkHandler;
import earth.terrarium.argonauts.common.registries.ModMenus;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.util.Objects;

public class Argonauts {
    public static final String MOD_ID = "argonauts";

    public static void init() {
        ModMenus.MENUS.init();
        NetworkHandler.init();
        if (isCadmusLoaded()) {
            CadmusIntegration.init();
        }
    }

    // Message of the day
    public static void onPlayerJoin(Player player) {
        if (!player.level().isClientSide()) {
            Guild guild = GuildHandler.get((ServerPlayer) player);
            if (guild == null) return;
            Component motd = guild.settings().motd();
            if (motd.getString().isEmpty()) return;

            player.displayClientMessage(ConstantComponents.MOTD_HEADER, false);
            player.displayClientMessage(motd, false);
            player.displayClientMessage(ConstantComponents.LINE, false);
        }
    }

    public static void onPlayerLeave(Player player) {
        if (!player.level().isClientSide()) {
            Party party = PartyHandler.getPlayerParty(player.getUUID());
            if (party == null) return;
            if (party.members().isLeader(player.getUUID())) {
                PartyHandler.disband(party, Objects.requireNonNull(player.getServer()));
            }
        }
    }

    public static boolean isCadmusLoaded() {
        return ModInfoUtils.isModLoaded("cadmus");
    }
}