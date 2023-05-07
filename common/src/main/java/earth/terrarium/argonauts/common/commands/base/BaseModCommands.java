package earth.terrarium.argonauts.common.commands.base;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.members.Group;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import earth.terrarium.argonauts.common.handlers.base.MemberPermissions;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

public final class BaseModCommands {
    public static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> tp(CommandHelper.GetGroupAction getGroupAction, MemberException exception1, MemberException exception2, AdditionalChecks additionalChecks) {
        return Commands.literal("tp")
            .then(Commands.argument("player", EntityArgument.player())
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    ServerPlayer target = EntityArgument.getPlayer(context, "player");
                    CommandHelper.runAction(() -> {
                        Group<?> group = getGroupAction.getGroup(player, false);
                        Group<?> otherGroup = getGroupAction.getGroup(target, true);
                        if (group != otherGroup) {
                            throw exception1;
                        }
                        Member member = group.getMember(player);
                        Member targetMember = group.getMember(target);
                        if (!member.hasPermission(MemberPermissions.TP_MEMBERS)) {
                            throw exception2;
                        }
                        additionalChecks.check(group, targetMember);
                        player.teleportTo(
                            target.getLevel(),
                            target.getX(), target.getY(), target.getZ(),
                            target.getYRot(), target.getXRot()
                        );
                    });
                    return 1;
                }));
    }

    public static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> warp(CommandHelper.GetGroupAction getGroupAction, MemberException exception1) {
        return Commands.literal("warp")
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                Group<?> group = getGroupAction.getGroup(player, false);
                CommandHelper.runAction(() -> {
                    Member member = group.getMember(player);
                    if (member.hasPermission(MemberPermissions.TP_MEMBERS)) {
                        tpAllMembers(group, player);
                    } else {
                        throw exception1;
                    }
                });
                return 1;
            });
    }

    public static void tpAllMembers(Group<?> group, ServerPlayer target) {
        PlayerList list = target.server.getPlayerList();
        for (Member member : group.members()) {
            if (member.profile().getId().equals(target.getUUID())) {
                continue;
            }
            ServerPlayer player = list.getPlayer(member.profile().getId());
            if (player != null) {
                player.teleportTo(target.getLevel(), target.getX(), target.getY(), target.getZ(), target.getYRot(), target.getXRot());
            }
        }
    }

    @FunctionalInterface
    public interface AdditionalChecks {
        void check(Group<?> group, Member targetMember) throws MemberException;
    }
}