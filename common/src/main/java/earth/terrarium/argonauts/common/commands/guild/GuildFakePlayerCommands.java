package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.datafixers.util.Pair;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.guild.Guild;
import earth.terrarium.argonauts.common.handlers.guild.members.GuildMember;
import earth.terrarium.argonauts.common.handlers.guild.members.GuildMembers;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public class GuildFakePlayerCommands {

    private static final SuggestionProvider<CommandSourceStack> SUGGESTION_PROVIDER = (context, builder) -> SharedSuggestionProvider.suggest(ModUtils.getFakePlayers(), builder, pair -> pair.getFirst().toString(), Pair::getSecond);
    private static final SuggestionProvider<CommandSourceStack> CURRENT_FAKE_PLAYERS_SUGGESTION_PROVIDER = (context, builder) -> {
        ServerPlayer player = context.getSource().getPlayerOrException();
        Guild guild = GuildCommandHelper.getGuildOrThrow(player, false);
        return SharedSuggestionProvider.suggest(((GuildMembers) guild.members()).fakePlayers().stream().map(UUID::toString), builder);
    };

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("guild")
            .then(Commands.literal("fakePlayers")
                .then(add())
                .then(remove())
            ));
    }

    public static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> add() {
        return Commands.literal("add")
            .then(Commands.argument("value", UuidArgument.uuid())
                .suggests(SUGGESTION_PROVIDER)
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    CommandHelper.runAction(() -> {
                        UUID id = UuidArgument.getUuid(context, "value");
                        Guild guild = GuildCommandHelper.getGuildOrThrow(player, false);
                        GuildMember guildMember = guild.members().get(player.getUUID());
                        if (guildMember == null) throw MemberException.YOU_ARE_NOT_IN_GUILD;
                        if (!guildMember.hasPermission(MemberPermissions.MANAGE_MEMBERS))
                            throw MemberException.YOU_CANT_MANAGE_MEMBERS_IN_GUILD;
                        ((GuildMembers) guild.members()).fakePlayers().add(id);
                    });
                    return 1;
                }));
    }

    public static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> remove() {
        return Commands.literal("remove")
            .then(Commands.argument("value", UuidArgument.uuid())
                .suggests(CURRENT_FAKE_PLAYERS_SUGGESTION_PROVIDER)
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    CommandHelper.runAction(() -> {
                        UUID id = UuidArgument.getUuid(context, "value");
                        Guild guild = GuildCommandHelper.getGuildOrThrow(player, false);
                        GuildMember guildMember = guild.members().get(player.getUUID());
                        if (guildMember == null) throw MemberException.YOU_ARE_NOT_IN_GUILD;
                        if (!guildMember.hasPermission(MemberPermissions.MANAGE_MEMBERS))
                            throw MemberException.YOU_CANT_MANAGE_MEMBERS_IN_GUILD;
                        ((GuildMembers) guild.members()).fakePlayers().remove(id);
                    });
                    return 1;
                }));
    }
}
