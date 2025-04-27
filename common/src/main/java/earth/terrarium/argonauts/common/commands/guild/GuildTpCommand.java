package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.events.ArgonautsEvents;
import earth.terrarium.argonauts.api.teams.guild.Guild;
import earth.terrarium.argonauts.api.teams.guild.GuildApi;
import earth.terrarium.argonauts.common.commands.TeamExceptions;
import earth.terrarium.argonauts.common.commands.TeamSuggestionProviders;
import earth.terrarium.argonauts.common.compat.prometheus.ArgonautsPermissions;
import earth.terrarium.argonauts.common.compat.prometheus.PrometheusCompat;
import earth.terrarium.argonauts.common.permissions.Permissions;
import earth.terrarium.argonauts.common.settings.Settings;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

public final class GuildTpCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("guild")
            .then(Commands.literal("tp")
                .then(Commands.argument("player", EntityArgument.player())
                    .suggests(TeamSuggestionProviders.CURRENT_GUILD_MEMBERS_SUGGESTION_PROVIDER)
                    .executes(context -> {
                        tp(context.getSource(), EntityArgument.getPlayer(context, "player"));
                        return 1;
                    })
                )
            )
        );
    }

    private static void tp(CommandSourceStack source, ServerPlayer target) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Guild guild = GuildApi.API.getPlayerGuild(player).orElse(null);
        if (guild == null) throw TeamExceptions.NOT_IN_GUILD.create();
        if (!guild.hasPermission(player.getUUID(), Permissions.TELEPORT)) throw TeamExceptions.NO_PERMISSION_TELEPORT.create();
        if (!Settings.PASSIVE_TELEPORT.get(guild)) throw TeamExceptions.PASSIVE_TELEPORT_DISABLED.create();
        if (Argonauts.IS_PROMETHEUS_LOADED && !PrometheusCompat.hasPermission(player, ArgonautsPermissions.TELEPORT)) throw TeamExceptions.NO_PERMISSION_TELEPORT.create();

        if (ArgonautsEvents.OnTeleport.fire(player, target.blockPosition())) {
            player.teleportTo(target.serverLevel(), target.getX(), target.getY(), target.getZ(), target.getYRot(), target.getXRot());
        }
    }
}
