package earth.terrarium.argonauts.common.commands.party;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import earth.terrarium.argonauts.common.commands.base.BaseCommands;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.party.Party;
import earth.terrarium.argonauts.common.handlers.party.PartyHandler;
import earth.terrarium.argonauts.common.handlers.party.members.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.party.members.PartyMember;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;
import java.util.function.Consumer;

public final class PartyCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("party")
            .then(join())
            .then(ignore())
            .then(unignore())
            .then(leave())
        );
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> leave() {
        return BaseCommands.leave(
            PartyCommandHelper::getPartyOrThrow,
            PartyHandler::leave);
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> join() {
        return BaseCommands.join(
            PartyCommandHelper::getPartyOrThrow,
            (group, player) -> PartyHandler.join((Party) group, player));
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> ignore() {
        return ignoreHelper("ignore");
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> unignore() {
        return ignoreHelper("unignore");
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> ignoreHelper(String subCommand) {
        return Commands.literal(subCommand).then(Commands.argument("members", GameProfileArgument.gameProfile())
            .executes(context -> {
                Player player = context.getSource().getPlayerOrException();
                Collection<GameProfile> profiles = GameProfileArgument.getGameProfiles(context, "members");
                Party party = PartyCommandHelper.getPartyOrThrow(player, false);
                CommandHelper.runAction(() -> {
                    PartyMember member = party.getMember(player);
                    if (member.hasPermission(MemberPermissions.MANAGE_MEMBERS)) {
                        Consumer<GameProfile> action = subCommand.equals("ignore") ? party.ignored()::add : party.ignored()::remove;
                        profiles.forEach(action);
                    } else {
                        throw MemberException.YOU_CANT_MANAGE_MEMBERS_IN_PARTY;
                    }
                });
                return 1;
            }));
    }
}
