package earth.terrarium.odyssey_allies.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.odyssey_allies.api.events.AlliesEvents;
import earth.terrarium.odyssey_allies.api.teams.party.Party;
import earth.terrarium.odyssey_allies.api.teams.party.PartyApi;
import earth.terrarium.odyssey_allies.common.commands.AlliesExcepetions;
import earth.terrarium.odyssey_allies.common.compat.roles.AlliesPermissions;
import earth.terrarium.odyssey_allies.common.compat.roles.RolesCompat;
import earth.terrarium.odyssey_allies.common.permissions.Permissions;
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
        if (party == null) throw AlliesExcepetions.NOT_IN_PARTY.create();
        if (!party.hasPermission(player.getUUID(), Permissions.TELEPORT_MEMBERS)) throw AlliesExcepetions.NO_PERMISSION_TELEPORT_MEMBERS.create();
        if (earth.terrarium.odyssey_allies.OdysseyAllies.IS_ROLES_LOADED && !RolesCompat.hasPermission(player, AlliesPermissions.TELEPORT)) throw AlliesExcepetions.NO_PERMISSION_TELEPORT.create();

        party.onlineMembers(source.getLevel())
            .stream()
            .filter(target -> !target.getUUID().equals(player.getUUID()))
            .forEach(target -> {
                if (AlliesEvents.OnTeleport.fire((ServerPlayer) target, player.blockPosition())) {
                    player.teleportTo(player.serverLevel(), player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
                }
            });
    }
}
