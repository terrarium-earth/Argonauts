package earth.terrarium.odyssey_guilds.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.odyssey_guilds.OdysseyGuilds;
import earth.terrarium.odyssey_guilds.api.teams.guild.Guild;
import earth.terrarium.odyssey_guilds.api.teams.guild.GuildApi;
import earth.terrarium.odyssey_guilds.common.commands.TeamExceptions;
import earth.terrarium.odyssey_guilds.common.compat.roles.OdysseyGuildsPermissions;
import earth.terrarium.odyssey_guilds.common.compat.roles.RolesCompat;
import earth.terrarium.odyssey_guilds.common.utils.ModUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public final class GuildCreateCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("guild")
            .then(Commands.literal("create")
                .then(Commands.argument("name", StringArgumentType.greedyString())
                    .executes(context -> {
                        create(context.getSource(), ModUtils.formatTextColors(StringArgumentType.getString(context, "name")));
                        return 1;
                    }))
                .executes(context -> {
                    String name = ModUtils.translatableWithStyle("command.odyssey_guilds.guild_name", context.getSource().getPlayerOrException().getGameProfile().getName()).getString();
                    create(context.getSource(), name);
                    return 1;
                })
            )
        );
    }

    private static void create(CommandSourceStack source, String name) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        if (GuildApi.API.getPlayerGuild(player).isPresent()) throw TeamExceptions.ALREADY_IN_GUILD.create();
        if (OdysseyGuilds.IS_ROLES_LOADED && !RolesCompat.hasPermission(player, OdysseyGuildsPermissions.CREATE_GUILD)) throw TeamExceptions.NO_PERMISSION_CREATE_GUILD.create();

        Guild guild = new Guild(player.getUUID(), name);
        GuildApi.API.create(source.getLevel(), guild);

        source.sendSuccess(() -> ModUtils.translatableWithStyle("command.odyssey_guilds.guild_create", guild.displayName()), false);
    }
}
