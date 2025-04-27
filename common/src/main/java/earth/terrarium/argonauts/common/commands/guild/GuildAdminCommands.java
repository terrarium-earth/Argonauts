package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import earth.terrarium.argonauts.api.teams.guild.Guild;
import earth.terrarium.argonauts.api.teams.guild.GuildApi;
import earth.terrarium.argonauts.common.commands.TeamExceptions;
import earth.terrarium.argonauts.common.guild.GuildSaveData;
import earth.terrarium.argonauts.common.permissions.Permissions;
import earth.terrarium.argonauts.common.settings.Settings;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class GuildAdminCommands {

    public static final SuggestionProvider<CommandSourceStack> GUILDS_SUGGESTION_PROVIDER = (context, builder) ->
        SharedSuggestionProvider.suggest(
            GuildApi.API.getAll(context.getSource().getLevel()),
            builder,
            guild -> guild.id().toString(),
            Guild::displayName
        );

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("guild")
            .requires(source -> source.hasPermission(2))
            .then(Commands.literal("admin")
                .then(Commands.literal("join")
                    .then(Commands.argument("id", UuidArgument.uuid())
                        .suggests(GUILDS_SUGGESTION_PROVIDER)
                        .executes(context -> {
                            join(context.getSource(), UuidArgument.getUuid(context, "id"));
                            return 1;
                        })
                    )
                )
                .then(Commands.literal("disband")
                    .then(Commands.argument("id", UuidArgument.uuid())
                        .suggests(GUILDS_SUGGESTION_PROVIDER)
                        .executes(context -> {
                            disband(context.getSource(), UuidArgument.getUuid(context, "id"));
                            return 1;
                        })
                    )
                )
                .then(Commands.literal("disbandall")
                    .executes(context -> {
                        disbandAll(context.getSource());
                        return 1;
                    })
                )
            )
        );
    }

    private static void join(CommandSourceStack source, UUID id) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Guild guild = GuildApi.API.get(source.getLevel(), id).orElse(null);
        if (guild == null) throw TeamExceptions.GUILD_DOES_NOT_EXIST.create();
        if (GuildApi.API.getPlayerGuild(player).isPresent()) throw TeamExceptions.ALREADY_IN_GUILD.create();

        GuildApi.API.join(source.getLevel(), guild, id);
        GuildApi.API.modifyPermission(source.getLevel(), guild, player.getUUID(), Permissions.OPERATOR, true);

        if (Settings.ANNOUNCE_JOIN.get(guild)) {
            guild.onlineMembers(source.getLevel())
                .stream()
                .filter(member -> !member.getUUID().equals(player.getUUID()))
                .forEach(member -> member.displayClientMessage(ModUtils.translatableWithStyle("command.argonauts.joined_guild", player.getName()), false));
        }
        source.sendSuccess(() -> ModUtils.translatableWithStyle("command.argonauts.join_guild", guild.displayName()), false);
    }

    private static void disband(CommandSourceStack source, UUID id) throws CommandSyntaxException {
        Guild guild = GuildApi.API.get(source.getLevel(), id).orElse(null);
        if (guild == null) throw TeamExceptions.GUILD_DOES_NOT_EXIST.create();
        GuildApi.API.disband(source.getLevel(), guild);
        source.sendSuccess(() -> ModUtils.translatableWithStyle("command.argonauts.guild_disband", guild.displayName()), false);
    }

    private static void disbandAll(CommandSourceStack source) {
        GuildApi.API.getAll(source.getLevel()).forEach(guild -> {
            GuildApi.API.disband(source.getLevel(), guild);
            var data = GuildSaveData.read(source.getLevel());
            data.guilds().clear();
            data.guildsByPlayer().clear();
            data.setDirty();
            source.sendSuccess(() -> ModUtils.translatableWithStyle("command.argonauts.guild_disband", guild.displayName()), false);
        });
    }
}
