package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.argonauts.api.teams.MemberStatus;
import earth.terrarium.argonauts.api.teams.guild.Guild;
import earth.terrarium.argonauts.api.teams.guild.GuildApi;
import earth.terrarium.argonauts.common.commands.TeamExceptions;
import earth.terrarium.argonauts.common.commands.TeamSuggestionProviders;
import earth.terrarium.argonauts.common.permissions.Permissions;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

public final class GuildTransferCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("guild")
            .then(Commands.literal("transfer")
                .then(Commands.argument("player", EntityArgument.player())
                    .suggests(TeamSuggestionProviders.CURRENT_GUILD_MEMBERS_SUGGESTION_PROVIDER)
                    .executes(context -> {
                        transfer(context.getSource(), EntityArgument.getPlayer(context, "player"));
                        return 1;
                    })
                )
            )
        );
    }

    private static void transfer(CommandSourceStack source, ServerPlayer targetPlayer) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Guild guild = GuildApi.API.getPlayerGuild(player).orElse(null);
        if (guild == null) throw TeamExceptions.NOT_IN_GUILD.create();
        if (!guild.isOwner(player.getUUID())) throw TeamExceptions.NOT_GUILD_OWNER.create();
        if (!guild.isMember(targetPlayer.getUUID())) throw TeamExceptions.PLAYER_NOT_IN_GUILD.create();
        if (player.getUUID().equals(targetPlayer.getUUID())) throw TeamExceptions.CANT_TRANSFER_TO_YOURSELF.create();

        GuildApi.API.modifyMember(source.getLevel(), guild, player.getUUID(), MemberStatus.MEMBER);
        GuildApi.API.modifyPermission(source.getLevel(), guild, player.getUUID(), Permissions.OPERATOR, true);
        GuildApi.API.modifyMember(source.getLevel(), guild, targetPlayer.getUUID(), MemberStatus.OWNER);

        source.sendSuccess(() -> ModUtils.translatableWithStyle("command.argonauts.transfer_guild", targetPlayer.getName()), false);
        targetPlayer.displayClientMessage(ModUtils.translatableWithStyle("command.argonauts.now_guild_owner", guild.displayName()), false);
    }
}
