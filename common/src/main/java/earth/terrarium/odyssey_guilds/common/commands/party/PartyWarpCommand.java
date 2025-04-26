package earth.terrarium.odyssey_guilds.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.odyssey_guilds.OdysseyGuilds;
import earth.terrarium.odyssey_guilds.api.events.OdysseyGuildsEvents;
import earth.terrarium.odyssey_guilds.api.teams.party.Party;
import earth.terrarium.odyssey_guilds.api.teams.party.PartyApi;
import earth.terrarium.odyssey_guilds.common.commands.TeamExceptions;
import earth.terrarium.odyssey_guilds.common.compat.prometheus.ArgonautsPermissions;
import earth.terrarium.odyssey_guilds.common.compat.prometheus.PrometheusCompat;
import earth.terrarium.odyssey_guilds.common.permissions.Permissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public final class PartyWarpCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("party")
            .then(Commands.literal("warp")
                .executes(context -> {
                    warp(context.getSource());
                    return 1;
                })
            )
        );
    }

    private static void warp(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Party party = PartyApi.API.getPlayerParty(player).orElse(null);
        if (party == null) throw TeamExceptions.NOT_IN_PARTY.create();
        if (!party.hasPermission(player.getUUID(), Permissions.TELEPORT_MEMBERS)) throw TeamExceptions.NO_PERMISSION_TELEPORT_MEMBERS.create();
        if (OdysseyGuilds.IS_ROLES_LOADED && !PrometheusCompat.hasPermission(player, ArgonautsPermissions.TELEPORT)) throw TeamExceptions.NO_PERMISSION_TELEPORT.create();

        party.onlineMembers(source.getLevel())
            .stream()
            .filter(target -> !target.getUUID().equals(player.getUUID()))
            .forEach(target -> {
                if (OdysseyGuildsEvents.OnTeleport.fire((ServerPlayer) target, player.blockPosition())) {
                    player.teleportTo(player.serverLevel(), player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
                }
            });
    }
}
