package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.compat.cadmus.CadmusIntegration;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.guild.Guild;
import earth.terrarium.argonauts.common.handlers.guild.GuildHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.ArrayList;
import java.util.Collection;

public class GuildAdminCommands {

    private static final SuggestionProvider<CommandSourceStack> GUILDS_SUGGESTION_PROVIDER = (context, builder) -> {
        Collection<Guild> guilds = GuildHandler.getAll(context.getSource().getServer());
        return SharedSuggestionProvider.suggest((guilds.stream().map(g -> g.id().toString())), builder);
    };

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("guild")
            .then(Commands.literal("admin")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("disband")
                    .then(Commands.argument("teamId", UuidArgument.uuid())
                        .suggests(GUILDS_SUGGESTION_PROVIDER)
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            Guild guild = GuildHandler.get(player.server, UuidArgument.getUuid(context, "teamId"));
                            removeGuild(guild, player);
                            return 1;
                        })))
                .then(Commands.literal("disbandAll")
                    .executes(context -> {
                        ServerPlayer player = context.getSource().getPlayerOrException();
                        new ArrayList<>(GuildHandler.getAll(player.server)).forEach(guild -> removeGuild(guild, player));
                        return 1;
                    }))
                .then(Commands.literal("join")
                    .then(Commands.argument("teamId", UuidArgument.uuid())
                        .suggests(GUILDS_SUGGESTION_PROVIDER)
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            Guild guild = GuildHandler.get(player.server, UuidArgument.getUuid(context, "teamId"));
                            joinGuild(guild, player);
                            return 1;
                        })))
            ));

        if (Argonauts.isCadmusLoaded()) {
            dispatcher.register(Commands.literal("guild")
                .then(Commands.literal("admin")
                    .requires(source -> source.hasPermission(2))
                    .then(Commands.literal("removeClaims")
                        .then(Commands.argument("teamId", UuidArgument.uuid())
                            .suggests(GUILDS_SUGGESTION_PROVIDER)
                            .executes(context -> {
                                ServerPlayer player = context.getSource().getPlayerOrException();
                                Guild guild = GuildHandler.get(player.server, UuidArgument.getUuid(context, "teamId"));
                                removeCadmusClaims(guild, player);
                                return 1;
                            })))
                    .then(Commands.literal("removeAllClaims")
                        .executes(context -> {
                            ServerPlayer player = context.getSource().getPlayerOrException();
                            new ArrayList<>(GuildHandler.getAll(player.server)).forEach(guild -> removeCadmusClaims(guild, player));
                            return 1;
                        }))
                ));
        }
    }

    public static void removeGuild(Guild guild, ServerPlayer player) {
        CommandHelper.runAction(() -> {
            if (guild == null) throw MemberException.GUILD_DOES_NOT_EXIST;
            player.displayClientMessage(Component.translatable("text.argonauts.member.guild_disband", guild.settings().displayName().getString()), false);
            guild.members().forEach(p -> {
                ServerPlayer groupMember = player.server.getPlayerList().getPlayer(p.profile().getId());
                if (groupMember != null) {
                    groupMember.displayClientMessage(Component.translatable("text.argonauts.member.disband", guild.displayName()), false);
                }
            });
            GuildHandler.remove(guild, player.server);
        });
    }

    public static void removeCadmusClaims(Guild guild, ServerPlayer player) {
        CommandHelper.runAction(() -> {
            if (guild == null) throw MemberException.GUILD_DOES_NOT_EXIST;
            player.displayClientMessage(Component.translatable("text.argonauts.cadmus.removed", CadmusIntegration.getChunksForGuild(guild, player.server), guild.settings().displayName().getString()), false);
            CadmusIntegration.disbandCadmusTeam(guild, player.server);
        });
    }

    public static void joinGuild(Guild guild, ServerPlayer player) {
        CommandHelper.runAction(() -> {
            if (guild == null) throw MemberException.GUILD_DOES_NOT_EXIST;
            GuildHandler.join(guild, player);
        });
    }
}
