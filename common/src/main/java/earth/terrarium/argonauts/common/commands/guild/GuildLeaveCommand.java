package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.argonauts.api.teams.guild.Guild;
import earth.terrarium.argonauts.api.teams.guild.GuildApi;
import earth.terrarium.argonauts.common.commands.TeamExceptions;
import earth.terrarium.argonauts.common.settings.Settings;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public final class GuildLeaveCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("guild")
            .then(Commands.literal("leave")
                .executes(context -> {
                    leave(context.getSource());
                    return 1;
                })
            )
        );
    }

    private static void leave(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Guild guild = GuildApi.API.getPlayerGuild(player).orElse(null);
        if (guild == null) throw TeamExceptions.NOT_IN_GUILD.create();

        GuildApi.API.leave(source.getLevel(), guild, player.getUUID());

        if (Settings.ANNOUNCE_LEAVE.get(guild)) {
            guild.onlineMembers(source.getLevel())
                .stream()
                .filter(member -> !member.getUUID().equals(player.getUUID()))
                .forEach(member -> member.displayClientMessage(ModUtils.translatableWithStyle("command.argonauts.left_guild", player.getName()), false));
        }
        source.sendSuccess(() -> ModUtils.translatableWithStyle("command.argonauts.leave_guild", guild.displayName()), false);
    }
}
