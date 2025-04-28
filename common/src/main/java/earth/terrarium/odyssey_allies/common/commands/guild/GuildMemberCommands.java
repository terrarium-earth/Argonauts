package earth.terrarium.odyssey_allies.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.odyssey_allies.api.teams.guild.Guild;
import earth.terrarium.odyssey_allies.api.teams.guild.GuildApi;
import earth.terrarium.odyssey_allies.common.commands.AlliesExcepetions;
import earth.terrarium.odyssey_allies.common.utils.ModUtils;
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
        if (guild == null) throw AlliesExcepetions.NOT_IN_GUILD.create();

        GameProfileCache profileCache = source.getServer().getProfileCache();
        if (profileCache == null) return;

        guild.members().forEach((id, member) ->
            profileCache.get(id).ifPresent(profile -> source.sendSuccess(() ->
                ModUtils.translatableWithStyle("command.odyssey_allies.list_member", profile.getName(), member.status().getDisplayName()), false)));
    }
}
