package earth.terrarium.odyssey_allies.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.odyssey_allies.api.events.AlliesEvents;
import earth.terrarium.odyssey_allies.api.teams.guild.Guild;
import earth.terrarium.odyssey_allies.api.teams.guild.GuildApi;
import earth.terrarium.odyssey_allies.common.commands.TeamExceptions;
import earth.terrarium.odyssey_allies.common.compat.roles.AlliesPermissions;
import earth.terrarium.odyssey_allies.common.compat.roles.RolesCompat;
import earth.terrarium.odyssey_allies.common.permissions.Permissions;
import earth.terrarium.odyssey_allies.common.settings.Settings;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

public final class GuildHeadquartersCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("guild")
            .then(Commands.literal("headquarters")
                .executes(context -> {
                    teleportToHeadquarters(context.getSource());
                    return 1;
                })
            )
            .then(Commands.literal("hq")
                .executes(context -> {
                    teleportToHeadquarters(context.getSource());
                    return 1;
                })
            )
        );
    }

    private static void teleportToHeadquarters(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Guild guild = GuildApi.API.getPlayerGuild(player).orElse(null);
        if (guild == null) throw TeamExceptions.NOT_IN_GUILD.create();
        if (!guild.hasPermission(player.getUUID(), Permissions.TELEPORT)) throw TeamExceptions.NO_PERMISSION_TELEPORT.create();
        if (earth.terrarium.odyssey_allies.OdysseyAllies.IS_ROLES_LOADED && !RolesCompat.hasPermission(player, AlliesPermissions.TELEPORT)) throw TeamExceptions.NO_PERMISSION_TELEPORT.create();

        GlobalPos hq = Settings.HEADQUARTERS.get(guild).orElse(null);
        if (hq == null) throw TeamExceptions.HEADQUARTERS_NOT_SET.create();

        ServerLevel targetLevel = player.server.getLevel(hq.dimension());
        if (targetLevel == null) return;

        if (AlliesEvents.OnTeleport.fire(player, hq.pos())) {
            player.teleportTo(
                targetLevel,
                hq.pos().getX(), hq.pos().getY(), hq.pos().getZ(),
                player.getYRot(), player.getXRot()
            );
        }
    }
}
