package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.events.ArgonautsEvents;
import earth.terrarium.argonauts.api.teams.guild.Guild;
import earth.terrarium.argonauts.api.teams.guild.GuildApi;
import earth.terrarium.argonauts.common.commands.TeamExceptions;
import earth.terrarium.argonauts.common.compat.prometheus.ArgonautsPermissions;
import earth.terrarium.argonauts.common.compat.prometheus.PrometheusCompat;
import earth.terrarium.argonauts.common.permissions.Permissions;
import earth.terrarium.argonauts.common.settings.Settings;
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
        if (Argonauts.IS_PROMETHEUS_LOADED && !PrometheusCompat.hasPermission(player, ArgonautsPermissions.TELEPORT)) throw TeamExceptions.NO_PERMISSION_TELEPORT.create();

        GlobalPos hq = Settings.HEADQUARTERS.get(guild).orElse(null);
        if (hq == null) throw TeamExceptions.HEADQUARTERS_NOT_SET.create();

        ServerLevel targetLevel = player.server.getLevel(hq.dimension());
        if (targetLevel == null) return;

        if (ArgonautsEvents.OnTeleport.fire(player, hq.pos())) {
            player.teleportTo(
                targetLevel,
                hq.pos().getX(), hq.pos().getY(), hq.pos().getZ(),
                player.getYRot(), player.getXRot()
            );
        }
    }
}
