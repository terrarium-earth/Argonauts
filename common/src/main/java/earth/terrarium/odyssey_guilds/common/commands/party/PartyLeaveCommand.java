package earth.terrarium.odyssey_guilds.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.odyssey_guilds.api.teams.party.Party;
import earth.terrarium.odyssey_guilds.api.teams.party.PartyApi;
import earth.terrarium.odyssey_guilds.common.commands.TeamExceptions;
import earth.terrarium.odyssey_guilds.common.settings.Settings;
import earth.terrarium.odyssey_guilds.common.utils.ModUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public final class PartyLeaveCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("party")
            .then(Commands.literal("leave")
                .executes(context -> {
                    leave(context.getSource());
                    return 1;
                })
            )
        );
    }

    private static void leave(CommandSourceStack source) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Party party = PartyApi.API.getPlayerParty(player).orElse(null);
        if (party == null) throw TeamExceptions.PLAYER_NOT_IN_PARTY.create();

        PartyApi.API.leave(source.getLevel(), party, player.getUUID());

        if (Settings.ANNOUNCE_LEAVE.get(party)) {
            party.onlineMembers(source.getLevel())
                .stream()
                .filter(member -> !member.getUUID().equals(player.getUUID()))
                .forEach(member -> member.displayClientMessage(ModUtils.translatableWithStyle("command.odyssey_guilds.left_party", player.getName()), false));
        }
        source.sendSuccess(() -> ModUtils.translatableWithStyle("command.odyssey_guilds.leave_party", party.displayName()), false);
    }
}
