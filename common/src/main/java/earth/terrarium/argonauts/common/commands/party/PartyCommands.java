package earth.terrarium.argonauts.common.commands.party;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.party.Party;
import earth.terrarium.argonauts.common.handlers.party.PartyHandler;
import earth.terrarium.argonauts.common.handlers.party.members.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.party.members.PartyMember;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.GameProfileArgument;
import net.minecraft.world.entity.player.Player;

import java.util.Collection;

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
        return Commands.literal("leave")
            .executes(context -> {
                Player player = context.getSource().getPlayerOrException();
                Party party = PartyCommandHelper.getPartyOrThrow(player, true);
                CommandHelper.runAction(() -> PartyHandler.remove(party.id(), player));
                return 1;
            });
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> join() {
        return Commands.literal("join").then(Commands.argument("player", EntityArgument.player())
            .executes(context -> {
                Player player = context.getSource().getPlayerOrException();
                Player target = EntityArgument.getPlayer(context, "player");
                Party party = PartyCommandHelper.getPartyOrThrow(target, true);
                CommandHelper.runAction(() -> PartyHandler.join(party, player));
                return 1;
            }));
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> ignore() {
        return Commands.literal("ignore").then(Commands.argument("members", GameProfileArgument.gameProfile())
            .executes(context -> {
                Player player = context.getSource().getPlayerOrException();
                Collection<GameProfile> profiles = GameProfileArgument.getGameProfiles(context, "members");
                Party party = PartyCommandHelper.getPartyOrThrow(player, false);
                CommandHelper.runAction(() -> {
                    PartyMember member = party.getMember(player);
                    if (member.hasPermission(MemberPermissions.MANAGE_MEMBERS)) {
                        profiles.forEach(party.ignored()::add);
                    } else {
                        throw MemberException.YOU_CANT_MANAGE_MEMBERS_IN_PARTY;
                    }
                });
                return 1;
            }));
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> unignore() {
        return Commands.literal("unignore").then(Commands.argument("members", GameProfileArgument.gameProfile())
            .executes(context -> {
                Player player = context.getSource().getPlayerOrException();
                Collection<GameProfile> profiles = GameProfileArgument.getGameProfiles(context, "members");
                Party party = PartyCommandHelper.getPartyOrThrow(player, false);
                CommandHelper.runAction(() -> {
                    PartyMember member = party.getMember(player);
                    if (member.hasPermission(MemberPermissions.MANAGE_MEMBERS)) {
                        profiles.forEach(party.ignored()::remove);
                    } else {
                        throw MemberException.YOU_CANT_MANAGE_MEMBERS_IN_PARTY;
                    }
                });
                return 1;
            }));
    }
}
