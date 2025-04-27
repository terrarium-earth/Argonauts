package earth.terrarium.odyssey_guilds.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.odyssey_guilds.api.teams.guild.Guild;
import earth.terrarium.odyssey_guilds.api.teams.guild.GuildApi;
import earth.terrarium.odyssey_guilds.common.commands.TeamExceptions;
import earth.terrarium.odyssey_guilds.common.commands.TeamSuggestionProviders;
import earth.terrarium.odyssey_guilds.common.settings.Settings;
import earth.terrarium.odyssey_guilds.common.utils.ModUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

public final class GuildJoinCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("guild")
            .then(Commands.literal("join")
                .then(Commands.argument("player", EntityArgument.player())
                    .suggests(TeamSuggestionProviders.PLAYERS_IN_GUILD_SUGGESTION_PROVIDER)
                    .executes(context -> {
                        join(context.getSource(), EntityArgument.getPlayer(context, "player"));
                        return 1;
                    })
                )
            )
        );
    }

    private static void join(CommandSourceStack source, ServerPlayer targetPlayer) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Guild guild = GuildApi.API.getPlayerGuild(targetPlayer).orElse(null);
        if (guild == null) throw TeamExceptions.PLAYER_NOT_IN_GUILD.create();
        if (GuildApi.API.getPlayerGuild(player).isPresent()) throw TeamExceptions.ALREADY_IN_GUILD.create();
        if (player.getUUID().equals(targetPlayer.getUUID())) throw TeamExceptions.NOT_IN_GUILD.create();
        if (!guild.isPublic() && !guild.isInvited(player.getUUID())) throw TeamExceptions.NOT_INVITED_TO_GUILD.create();

        GuildApi.API.join(source.getLevel(), guild, player.getUUID());

        if (Settings.ANNOUNCE_JOIN.get(guild)) {
            guild.onlineMembers(source.getLevel())
                .stream()
                .filter(member -> !member.getUUID().equals(player.getUUID()))
                .forEach(member -> member.displayClientMessage(ModUtils.translatableWithStyle("command.odyssey_guilds.joined_guild", player.getName()), false));
        }
        source.sendSuccess(() -> ModUtils.translatableWithStyle("command.odyssey_guilds.join_guild", guild.displayName()), false);
    }
}
