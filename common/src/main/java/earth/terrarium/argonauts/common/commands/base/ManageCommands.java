package earth.terrarium.argonauts.common.commands.base;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.teamresourceful.resourcefullib.common.utils.CommonUtils;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.base.members.Group;
import earth.terrarium.argonauts.common.handlers.base.members.Member;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public final class ManageCommands {

    public static <M extends Member, T extends Group<M, ?>> ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> invite(String kind, MemberException youCantManageMembersException, MemberException alreadyInGroupException, CommandHelper.GetGroupAction<M, T> groupAction) {
        return Commands.literal("invite").then(Commands.argument("player", EntityArgument.player())
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                ServerPlayer target = EntityArgument.getPlayer(context, "player");
                var group = groupAction.getGroup(player, false);
                CommandHelper.runAction(() -> {
                    Member member = group.getMember(player);
                    if (player.getUUID().equals(target.getUUID())) {
                        throw MemberException.YOU_CANT_INVITE_YOURSELF;
                    }
                    if (group.members().isMember(target.getUUID())) {
                        throw alreadyInGroupException;
                    }
                    if (!member.hasPermission(MemberPermissions.MANAGE_MEMBERS)) {
                        throw youCantManageMembersException;
                    }

                    group.members().invite(target.getGameProfile());
                    player.displayClientMessage(CommonUtils.serverTranslatable("text.argonauts.invited", target.getName().getString()), false);
                    target.displayClientMessage(CommonUtils.serverTranslatable("text.argonauts.member." + kind + "_invite", player.getName().getString()), false);
                    target.displayClientMessage(ConstantComponents.CLICK_HERE_TO_JOIN.copy().withStyle(Style.EMPTY
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, CommonUtils.serverTranslatable("text.argonauts.member.join", group.displayName())))
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + kind + " join " + player.getGameProfile().getName()))), false);
                });
                return 1;
            }));
    }

    public static <M extends Member, T extends Group<M, ?>> ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> remove(MemberException youCantRemoveYourselfFromGroupException, MemberException youCantRemoveGroupLeaderException, MemberException youCantManageMembersInGroupException, CommandHelper.GetGroupAction<M, T> groupAction, RemoveAction action) {
        return Commands.literal("remove").then(Commands.argument("player", EntityArgument.player())
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                ServerPlayer target = EntityArgument.getPlayer(context, "player");
                var group = groupAction.getGroup(player, false);
                CommandHelper.runAction(() -> {
                    Member member = group.getMember(player);
                    if (!member.hasPermission(MemberPermissions.MANAGE_MEMBERS)) {
                        throw youCantManageMembersInGroupException;
                    }
                    if (player.getUUID().equals(target.getUUID())) {
                        throw youCantRemoveYourselfFromGroupException;
                    }
                    if (group.members().isLeader(target.getUUID())) {
                        throw youCantRemoveGroupLeaderException;
                    }

                    action.remove(group.id(), target);
                });
                return 1;
            }));
    }

    @FunctionalInterface
    public interface RemoveAction {
        void remove(UUID uuid, ServerPlayer player) throws MemberException;
    }
}
