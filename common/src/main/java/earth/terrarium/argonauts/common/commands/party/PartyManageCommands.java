package earth.terrarium.argonauts.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.handlers.party.Party;
import earth.terrarium.argonauts.common.handlers.party.PartyException;
import earth.terrarium.argonauts.common.handlers.party.PartyHandler;
import earth.terrarium.argonauts.common.handlers.party.members.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.party.members.PartyMember;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.world.entity.player.Player;

public final class PartyManageCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("party")
            .then(invite())
            .then(remove())
        );
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> invite() {
        return Commands.literal("invite").then(Commands.argument("player", EntityArgument.player())
            .executes(context -> {
                Player player = context.getSource().getPlayerOrException();
                Player target = EntityArgument.getPlayer(context, "player");
                Party party = PartyCommandHelper.getPartyOrThrow(player, false);
                CommandHelper.runAction(() -> {
                    PartyMember member = party.getMember(player);
                    if (member.hasPermission(MemberPermissions.MANAGE_MEMBERS)) {
                        party.members().invite(target.getGameProfile());
                        //TODO send message to target
                    } else {
                        throw PartyException.YOU_CANT_MANAGE_MEMBERS;
                    }
                });
                return 1;
            }));
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> remove() {
        return Commands.literal("remove").then(Commands.argument("player", EntityArgument.player())
            .executes(context -> {
                Player player = context.getSource().getPlayerOrException();
                Player target = EntityArgument.getPlayer(context, "player");
                Party party = PartyCommandHelper.getPartyOrThrow(player, false);
                CommandHelper.runAction(() -> {
                    PartyMember member = party.getMember(player);
                    if (member.hasPermission(MemberPermissions.MANAGE_MEMBERS)) {
                        if (player.getUUID().equals(target.getUUID())) {
                            throw PartyException.YOU_CANT_REMOVE_YOURSELF;
                        }
                        PartyHandler.remove(party.id(), target);
                    } else {
                        throw PartyException.YOU_CANT_MANAGE_MEMBERS;
                    }
                });
                return 1;
            }));
    }
}
