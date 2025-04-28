package earth.terrarium.odyssey_allies.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.odyssey_allies.api.events.AlliesEvents;
import earth.terrarium.odyssey_allies.api.teams.guild.Guild;
import earth.terrarium.odyssey_allies.api.teams.guild.GuildApi;
import earth.terrarium.odyssey_allies.common.commands.TeamExceptions;
import earth.terrarium.odyssey_allies.common.commands.TeamSuggestionProviders;
import earth.terrarium.odyssey_allies.common.compat.roles.AlliesPermissions;
import earth.terrarium.odyssey_allies.common.compat.roles.RolesCompat;
import earth.terrarium.odyssey_allies.common.permissions.Permissions;
import earth.terrarium.odyssey_allies.common.settings.Settings;
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
        if (earth.terrarium.odyssey_allies.OdysseyAllies.IS_ROLES_LOADED && !RolesCompat.hasPermission(player, AlliesPermissions.TELEPORT)) throw TeamExceptions.NO_PERMISSION_TELEPORT.create();

        if (AlliesEvents.OnTeleport.fire(player, target.blockPosition())) {
            player.teleportTo(target.serverLevel(), target.getX(), target.getY(), target.getZ(), target.getYRot(), target.getXRot());
        }
    }
}
