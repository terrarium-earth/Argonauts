package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.argonauts.api.teams.guild.Guild;
import earth.terrarium.argonauts.api.teams.guild.GuildApi;
import earth.terrarium.argonauts.api.teams.permissions.MemberPermissionsApi;
import earth.terrarium.argonauts.common.commands.TeamExceptions;
import earth.terrarium.argonauts.common.commands.TeamSuggestionProviders;
import earth.terrarium.argonauts.common.utils.ModUtils;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

public final class GuildPermissionCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        MemberPermissionsApi.API.getGuildPermissions().forEach((permission, defaultValue) ->
            dispatcher.register(Commands.literal("guild")
                .then(Commands.literal("permissions")
                    .then(Commands.literal("set")
                        .then(Commands.literal(permission)
                            .then(Commands.argument("player", EntityArgument.player())
                                .suggests(TeamSuggestionProviders.CURRENT_GUILD_MEMBERS_SUGGESTION_PROVIDER)
                                .then(Commands.argument("value", BoolArgumentType.bool())
                                    .executes(context -> {
                                        boolean value = BoolArgumentType.getBool(context, "value");
                                        set(context.getSource(), EntityArgument.getPlayer(context, "player"), permission, value);
                                        return 1;
                                    })
                                )
                            )
                        )
                    )
                    .then(Commands.literal("get")
                        .then(Commands.literal(permission)
                            .then(Commands.argument("player", EntityArgument.player())
                                .suggests(TeamSuggestionProviders.CURRENT_GUILD_MEMBERS_SUGGESTION_PROVIDER)
                                .executes(context -> {
                                    get(context.getSource(), EntityArgument.getPlayer(context, "player"), permission);
                                    return 1;
                                })
                            )
                        )
                    )
                    .then(Commands.literal("list")
                        .then(Commands.argument("player", EntityArgument.player())
                            .suggests(TeamSuggestionProviders.CURRENT_GUILD_MEMBERS_SUGGESTION_PROVIDER)
                            .executes(context -> {
                                list(context.getSource(), EntityArgument.getPlayer(context, "player"));
                                return 1;
                            })
                        )
                    )
                )
            )
        );
    }

    private static void set(CommandSourceStack source, ServerPlayer target, String permission, boolean value) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Guild guild = GuildApi.API.getPlayerGuild(player).orElse(null);
        if (guild == null) throw TeamExceptions.NOT_IN_GUILD.create();
        if (!guild.canManagePermissions(player.getUUID())) throw TeamExceptions.NO_PERMISSION_MANAGE_PERMISSIONS.create();

        GuildApi.API.modifyPermission(source.getLevel(), guild, target.getUUID(), permission, value);

        source.sendSuccess(() -> ModUtils.translatableWithStyle("command.argonauts.permissions.set",
            MemberPermissionsApi.API.getPermissionName(permission),
            value,
            target.getName()
        ), false);
    }

    private static void get(CommandSourceStack source, ServerPlayer target, String permission) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Guild guild = GuildApi.API.getPlayerGuild(player).orElse(null);
        if (guild == null) throw TeamExceptions.NOT_IN_GUILD.create();

        boolean value = guild.hasPermission(target.getUUID(), permission);

        source.sendSuccess(() -> ModUtils.translatableWithStyle("command.argonauts.permissions.get",
            MemberPermissionsApi.API.getPermissionName(permission),
            value,
            target.getName()
        ), false);
    }

    private static void list(CommandSourceStack source, ServerPlayer target) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Guild guild = GuildApi.API.getPlayerGuild(player).orElse(null);
        if (guild == null) throw TeamExceptions.NOT_IN_GUILD.create();

        Object2BooleanMap<String> permissions = guild.getOrCreateMember(target.getUUID()).permissions();

        permissions.forEach((permission, value) -> source.sendSuccess(() ->
            ModUtils.translatableWithStyle("command.argonauts.permissions.list",
                MemberPermissionsApi.API.getPermissionName(permission),
                value
            ), false));
    }
}
