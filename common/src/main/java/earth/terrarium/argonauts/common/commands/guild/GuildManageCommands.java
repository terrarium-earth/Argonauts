package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.teamresourceful.resourcefullib.common.utils.CommonUtils;
import earth.terrarium.argonauts.api.guild.Guild;
import earth.terrarium.argonauts.api.guild.GuildApi;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.commands.base.ManageCommands;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.guild.members.GuildMember;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

public final class GuildManageCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("guild")
            .then(invite())
            .then(remove())
            .then(Commands.literal("allies")
                .then(addAlly())
                .then(removeAlly())
            )
        );
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> invite() {
        return ManageCommands.invite(
            "guild",
            MemberException.YOU_CANT_MANAGE_MEMBERS_IN_GUILD,
            MemberException.PLAYER_ALREADY_IN_GUILD,
            GuildCommandHelper::getGuildOrThrow
        );
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> remove() {
        return ManageCommands.remove(
            MemberException.YOU_CANT_REMOVE_YOURSELF_FROM_GUILD,
            MemberException.YOU_CANT_REMOVE_GUILD_OWNER,
            MemberException.YOU_CANT_MANAGE_MEMBERS_IN_GUILD,
            GuildCommandHelper::getGuildOrThrow,
            GuildApi.API::leave
        );
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> addAlly() {
        return Commands.literal("add").then(Commands.argument("player", EntityArgument.player())
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                ServerPlayer target = EntityArgument.getPlayer(context, "player");
                Guild guild = GuildCommandHelper.getGuildOrThrow(player, false);

                CommandHelper.runAction(() -> {
                    GuildMember member = guild.getMember(player);
                    if (player.getUUID().equals(target.getUUID())) {
                        throw MemberException.YOU_CANT_ALLY_YOURSELF;
                    }
                    if (guild.members().isMember(target.getUUID())) {
                        throw MemberException.PLAYER_ALREADY_IN_GUILD;
                    }
                    if (!member.hasPermission(MemberPermissions.MANAGE_MEMBERS)) {
                        throw MemberException.YOU_CANT_MANAGE_MEMBERS_IN_GUILD;
                    }

                    guild.members().ally(target.getGameProfile());
                    player.displayClientMessage(CommonUtils.serverTranslatable("text.argonauts.allied", target.getName().getString()), false);
                    target.displayClientMessage(CommonUtils.serverTranslatable("text.argonauts.member.guild_allied", player.getName().getString()), false);
                });
                return 1;
            }));
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> removeAlly() {
        return Commands.literal("remove").then(Commands.argument("player", EntityArgument.player())
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                ServerPlayer target = EntityArgument.getPlayer(context, "player");
                Guild guild = GuildCommandHelper.getGuildOrThrow(player, false);
                CommandHelper.runAction(() -> {
                    GuildMember member = guild.getMember(player);
                    if (!member.hasPermission(MemberPermissions.MANAGE_MEMBERS)) {
                        throw MemberException.YOU_CANT_MANAGE_MEMBERS_IN_GUILD;
                    }
                    if (player.getUUID().equals(target.getUUID())) {
                        throw MemberException.YOU_CANT_REMOVE_YOURSELF_FROM_GUILD;
                    }
                    if (guild.members().isLeader(target.getUUID())) {
                        throw MemberException.YOU_CANT_REMOVE_GUILD_OWNER;
                    }
                    if (!guild.members().isAllied(target.getUUID())) {
                        throw MemberException.NOT_AN_ALLY;
                    }

                    GuildApi.API.leave(guild.id(), target);
                });
                return 1;
            }));
    }
}
