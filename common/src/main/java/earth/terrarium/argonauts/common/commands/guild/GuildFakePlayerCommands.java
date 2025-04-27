package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.datafixers.util.Pair;
import earth.terrarium.argonauts.api.teams.MemberStatus;
import earth.terrarium.argonauts.api.teams.guild.Guild;
import earth.terrarium.argonauts.api.teams.guild.GuildApi;
import earth.terrarium.argonauts.common.commands.TeamExceptions;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.GameProfileCache;

import java.util.UUID;

public final class GuildFakePlayerCommands {

    private static final SuggestionProvider<CommandSourceStack> FAKE_PLAYER_SUGGESTION_PROVIDER = (context, builder) ->
        SharedSuggestionProvider.suggest(ModUtils.getFakePlayers(), builder, pair -> pair.getFirst().toString(), Pair::getSecond);

    private static final SuggestionProvider<CommandSourceStack> CURRENT_FAKE_PLAYERS_SUGGESTION_PROVIDER = (context, builder) -> {
        ServerPlayer player = context.getSource().getPlayerOrException();
        Guild guild = GuildApi.API.getPlayerGuild(player).orElse(null);
        if (guild == null) throw TeamExceptions.NOT_IN_GUILD.create();
        return SharedSuggestionProvider.suggest(guild.getFakePlayers().stream().map(UUID::toString), builder);
    };

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("guild")
            .then(Commands.literal("fakeplayers")
                .then(Commands.literal("add")
                    .then(Commands.argument("uuid", UuidArgument.uuid())
                        .suggests(FAKE_PLAYER_SUGGESTION_PROVIDER)
                        .executes(context -> {
                            add(context.getSource(), UuidArgument.getUuid(context, "uuid"));
                            return 1;
                        })
                    )
                )
                .then(Commands.literal("remove")
                    .then(Commands.argument("uuid", UuidArgument.uuid())
                        .suggests(CURRENT_FAKE_PLAYERS_SUGGESTION_PROVIDER)
                        .executes(context -> {
                            remove(context.getSource(), UuidArgument.getUuid(context, "uuid"));
                            return 1;
                        })
                    )
                )
                .then(Commands.literal("list")
                    .executes(context -> {
                        list(context.getSource());
                        return 1;
                    })
                )
                .executes(context -> {
                    list(context.getSource());
                    return 1;
                })
            )
        );
    }

    private static void add(CommandSourceStack source, UUID fakePlayerId) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Guild guild = GuildApi.API.getPlayerGuild(player).orElse(null);
        if (guild == null) throw TeamExceptions.NOT_IN_GUILD.create();
        if (!guild.canManageMembers(player.getUUID())) throw TeamExceptions.NO_PERMISSION_MANAGE_MEMBERS.create();
        if (guild.isMemberOrFakePlayer(fakePlayerId)) throw TeamExceptions.FAKE_PLAYER_ALREADY_IN_GUILD.create();

        GuildApi.API.modifyMember(source.getLevel(), guild, fakePlayerId, MemberStatus.FAKE_PLAYER);

        source.sendSuccess(() -> ModUtils.translatableWithStyle("command.argonauts.add_fake_player", fakePlayerId), false);
    }

    private static void remove(CommandSourceStack source, UUID fakePlayerId) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Guild guild = GuildApi.API.getPlayerGuild(player).orElse(null);
        if (guild == null) throw TeamExceptions.NOT_IN_GUILD.create();
        if (!guild.canManageMembers(player.getUUID())) throw TeamExceptions.NO_PERMISSION_MANAGE_MEMBERS.create();
        if (!guild.isMemberOrFakePlayer(fakePlayerId)) throw TeamExceptions.FAKE_PLAYER_NOT_IN_GUILD.create();

        GuildApi.API.leave(source.getLevel(), guild, fakePlayerId);

        source.sendSuccess(() -> ModUtils.translatableWithStyle("command.argonauts.remove_fake_player", fakePlayerId), false);
    }

    private static void list(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Guild guild = GuildApi.API.getPlayerGuild(player).orElse(null);
        if (guild == null) throw TeamExceptions.NOT_IN_GUILD.create();

        GameProfileCache profileCache = source.getServer().getProfileCache();
        if (profileCache == null) return;

        guild.members().forEach((id, member) -> {
            if (member.status() == MemberStatus.FAKE_PLAYER) {
                profileCache.get(id).ifPresent(profile -> source.sendSuccess(() ->
                    ModUtils.translatableWithStyle("command.argonauts.list_fake_player", profile.getName()), false));
            }
        });
    }
}
