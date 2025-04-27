package earth.terrarium.odyssey_guilds.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.odyssey_guilds.api.teams.MemberStatus;
import earth.terrarium.odyssey_guilds.api.teams.guild.Guild;
import earth.terrarium.odyssey_guilds.api.teams.guild.GuildApi;
import earth.terrarium.odyssey_guilds.common.commands.TeamExceptions;
import earth.terrarium.odyssey_guilds.common.constants.ConstantComponents;
import earth.terrarium.odyssey_guilds.common.utils.ModUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

public final class GuildInviteCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("guild")
            .then(Commands.literal("invite")
                .then(Commands.argument("player", EntityArgument.player())
                    .executes(context -> {
                        invite(context.getSource(), EntityArgument.getPlayer(context, "player"));
                        return 1;
                    })
                )
            )
        );
    }

    private static void invite(CommandSourceStack source, ServerPlayer targetPlayer) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Guild guild = GuildApi.API.getPlayerGuild(player).orElse(null);
        if (guild == null) throw TeamExceptions.NOT_IN_GUILD.create();
        if (player.getUUID().equals(targetPlayer.getUUID())) throw TeamExceptions.CANT_INVITE_YOURSELF.create();
        if (!guild.isPublic() && !guild.canManageMembers(player.getUUID())) throw TeamExceptions.NO_PERMISSION_MANAGE_MEMBERS.create();
        if (guild.isMember(targetPlayer.getUUID())) throw TeamExceptions.PLAYER_IS_GUILD_MEMBER.create();
        if (guild.realMembersCount() >= GuildApi.API.getMaxGuildMembers(source.getLevel(), player.getUUID())) throw TeamExceptions.GUILD_FULL.create();

        GuildApi.API.modifyMember(source.getLevel(), guild, targetPlayer.getUUID(), MemberStatus.INVITED);

        source.sendSuccess(() -> ModUtils.translatableWithStyle("command.odyssey_guilds.invite", targetPlayer.getName()), false);
        targetPlayer.displayClientMessage(ModUtils.translatableWithStyle("command.odyssey_guilds.guild_invited", player.getName(), guild.displayName()), false);
        targetPlayer.displayClientMessage(ConstantComponents.CLICK_TO_ACCEPT.copy().withStyle(Style.EMPTY
            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ModUtils.translatableWithStyle("command.odyssey_guilds.join", guild.displayName())))
            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/guild join " + player.getGameProfile().getName()))), false);
    }
}
