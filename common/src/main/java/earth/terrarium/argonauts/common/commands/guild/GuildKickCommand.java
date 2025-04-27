package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.argonauts.api.teams.guild.Guild;
import earth.terrarium.argonauts.api.teams.guild.GuildApi;
import earth.terrarium.argonauts.common.commands.TeamExceptions;
import earth.terrarium.argonauts.common.commands.TeamSuggestionProviders;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

public final class GuildKickCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("guild")
            .then(Commands.literal("kick")
                .then(Commands.argument("player", EntityArgument.player())
                    .suggests(TeamSuggestionProviders.CURRENT_GUILD_MEMBERS_SUGGESTION_PROVIDER)
                    .executes(context -> {
                        kick(context.getSource(), EntityArgument.getPlayer(context, "player"));
                        return 1;
                    })
                )
            )
        );
    }

    private static void kick(CommandSourceStack source, ServerPlayer targetPlayer) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Guild guild = GuildApi.API.getPlayerGuild(player).orElse(null);
        if (guild == null) throw TeamExceptions.NOT_IN_GUILD.create();
        if (guild.isAllied(targetPlayer.getUUID())) {
            GuildAllyCommands.remove(source, targetPlayer);
            return;
        }
        if (!guild.canManageMembers(player.getUUID())) throw TeamExceptions.NO_PERMISSION_MANAGE_MEMBERS.create();
        if (!guild.isMember(targetPlayer.getUUID())) throw TeamExceptions.PLAYER_NOT_IN_GUILD.create();
        if (player.getUUID().equals(targetPlayer.getUUID())) throw TeamExceptions.CANT_KICK_YOURSELF.create();

        GuildApi.API.leave(source.getLevel(), guild, targetPlayer.getUUID());

        source.sendSuccess(() -> ModUtils.translatableWithStyle("command.argonauts.kick", targetPlayer.getName()), false);
        targetPlayer.displayClientMessage(ModUtils.translatableWithStyle("command.argonauts.guild_kicked", player.getName(), guild.displayName()), false);
    }
}
