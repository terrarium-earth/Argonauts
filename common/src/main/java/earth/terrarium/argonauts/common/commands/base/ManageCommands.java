package earth.terrarium.argonauts.common.commands.base;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.members.Group;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import earth.terrarium.argonauts.common.handlers.party.PartyHandler;
import earth.terrarium.argonauts.common.handlers.party.members.MemberPermissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public final class ManageCommands {

    public static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> invite(MemberException exception, CommandHelper.GetGroupAction groupAction) {
        return Commands.literal("invite").then(Commands.argument("player", EntityArgument.player())
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                ServerPlayer target = EntityArgument.getPlayer(context, "player");
                Group<?> group = groupAction.getGroup(player);
                CommandHelper.runAction(() -> {
                    Member member = group.getMember(player);
                    if (member.hasPermission(MemberPermissions.MANAGE_MEMBERS)) {
                        group.members().invite(target.getGameProfile());
                        //TODO send message to target
                    } else {
                        throw exception;
                    }
                });
                return 1;
            }));
    }

    public static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> remove(MemberException exception1, MemberException exception2, CommandHelper.GetGroupAction groupAction, RemoveAction action) {
        return Commands.literal("remove").then(Commands.argument("player", EntityArgument.player())
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                ServerPlayer target = EntityArgument.getPlayer(context, "player");
                Group<?> group = groupAction.getGroup(player);
                CommandHelper.runAction(() -> {
                    Member member = group.getMember(player);
                    if (member.hasPermission(MemberPermissions.MANAGE_MEMBERS)) {
                        if (player.getUUID().equals(target.getUUID())) {
                            throw exception1;
                        }
                        PartyHandler.remove(group.id(), target);
                        action.remove(group.id(), target);
                    } else {
                        throw exception2;
                    }
                });
                return 1;
            }));
    }

    @FunctionalInterface
    public interface RemoveAction {
        void remove(UUID uuid, ServerPlayer player) throws MemberException;
    }
}
