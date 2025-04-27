package earth.terrarium.argonauts.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.argonauts.api.teams.MemberStatus;
import earth.terrarium.argonauts.api.teams.party.Party;
import earth.terrarium.argonauts.api.teams.party.PartyApi;
import earth.terrarium.argonauts.common.commands.TeamExceptions;
import earth.terrarium.argonauts.common.commands.TeamSuggestionProviders;
import earth.terrarium.argonauts.common.permissions.Permissions;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

public final class PartyTransferCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("party")
            .then(Commands.literal("transfer")
                .then(Commands.argument("player", EntityArgument.player())
                    .suggests(TeamSuggestionProviders.CURRENT_PARTY_MEMBERS_SUGGESTION_PROVIDER)
                    .executes(context -> {
                        transfer(context.getSource(), EntityArgument.getPlayer(context, "player"));
                        return 1;
                    })
                )
            )
        );
    }

    private static void transfer(CommandSourceStack source, ServerPlayer targetPlayer) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Party party = PartyApi.API.getPlayerParty(player).orElse(null);
        if (party == null) throw TeamExceptions.NOT_IN_PARTY.create();
        if (!party.isOwner(player.getUUID())) throw TeamExceptions.NOT_PARTY_OWNER.create();
        if (!party.isMember(targetPlayer.getUUID())) throw TeamExceptions.PLAYER_NOT_IN_PARTY.create();
        if (player.getUUID().equals(targetPlayer.getUUID())) throw TeamExceptions.CANT_TRANSFER_TO_YOURSELF.create();

        PartyApi.API.modifyMember(source.getLevel(), party, player.getUUID(), MemberStatus.MEMBER);
        PartyApi.API.modifyPermission(source.getLevel(), party, player.getUUID(), Permissions.OPERATOR, true);
        PartyApi.API.modifyMember(source.getLevel(), party, targetPlayer.getUUID(), MemberStatus.OWNER);

        source.sendSuccess(() -> ModUtils.translatableWithStyle("command.argonauts.transfer_party", targetPlayer.getName()), false);
        targetPlayer.displayClientMessage(ModUtils.translatableWithStyle("command.argonauts.now_party_owner", party.displayName()), false);
    }
}
