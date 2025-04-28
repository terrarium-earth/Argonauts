package earth.terrarium.odyssey_allies.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.odyssey_allies.api.teams.guild.Guild;
import earth.terrarium.odyssey_allies.api.teams.guild.GuildApi;
import earth.terrarium.odyssey_allies.common.commands.AlliesExcepetions;
import earth.terrarium.odyssey_allies.common.compat.roles.AlliesPermissions;
import earth.terrarium.odyssey_allies.common.compat.roles.RolesCompat;
import earth.terrarium.odyssey_allies.common.utils.ModUtils;
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
                    String name = ModUtils.translatableWithStyle("command.odyssey_allies.guild_name", context.getSource().getPlayerOrException().getGameProfile().getName()).getString();
                    create(context.getSource(), name);
                    return 1;
                })
            )
        );
    }

    private static void create(CommandSourceStack source, String name) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        if (GuildApi.API.getPlayerGuild(player).isPresent()) throw AlliesExcepetions.ALREADY_IN_GUILD.create();
        if (earth.terrarium.odyssey_allies.OdysseyAllies.IS_ROLES_LOADED && !RolesCompat.hasPermission(player, AlliesPermissions.CREATE_GUILD)) throw AlliesExcepetions.NO_PERMISSION_CREATE_GUILD.create();

        Guild guild = new Guild(player.getUUID(), name);
        GuildApi.API.create(source.getLevel(), guild);

        source.sendSuccess(() -> ModUtils.translatableWithStyle("command.odyssey_allies.guild_create", guild.displayName()), false);
    }
}
