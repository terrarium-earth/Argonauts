package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.argonauts.api.teams.guild.Guild;
import earth.terrarium.argonauts.api.teams.guild.GuildApi;
import earth.terrarium.argonauts.common.commands.TeamExceptions;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.GameProfileCache;

public final class GuildMemberCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("guild")
            .then(Commands.literal("members")
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

    private static void list(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Guild guild = GuildApi.API.getPlayerGuild(player).orElse(null);
        if (guild == null) throw TeamExceptions.NOT_IN_GUILD.create();

        GameProfileCache profileCache = source.getServer().getProfileCache();
        if (profileCache == null) return;

        guild.members().forEach((id, member) ->
            profileCache.get(id).ifPresent(profile -> source.sendSuccess(() ->
                ModUtils.translatableWithStyle("command.argonauts.list_member", profile.getName(), member.status().getDisplayName()), false)));
    }
}
