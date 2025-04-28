package earth.terrarium.odyssey_allies.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.odyssey_allies.api.teams.party.Party;
import earth.terrarium.odyssey_allies.api.teams.party.PartyApi;
import earth.terrarium.odyssey_allies.common.commands.TeamExceptions;
import earth.terrarium.odyssey_allies.common.compat.roles.AlliesPermissions;
import earth.terrarium.odyssey_allies.common.compat.roles.RolesCompat;
import earth.terrarium.odyssey_allies.common.utils.ModUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public final class PartyCreateCommand {


    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("party")
            .then(Commands.literal("create")
                .then(Commands.argument("name", StringArgumentType.greedyString())
                    .executes(context -> {
                        create(context.getSource(), ModUtils.formatTextColors(StringArgumentType.getString(context, "name")));
                        return 1;
                    }))
                .executes(context -> {
                    String name = ModUtils.translatableWithStyle("command.odyssey_allies.party_name", context.getSource().getPlayerOrException().getName()).getString();
                    create(context.getSource(), name);
                    return 1;
                })
            )
        );
    }

    private static void create(CommandSourceStack source, String name) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        if (PartyApi.API.getPlayerParty(player).isPresent()) throw TeamExceptions.ALREADY_IN_PARTY.create();
        if (earth.terrarium.odyssey_allies.OdysseyAllies.IS_ROLES_LOADED && !RolesCompat.hasPermission(player, AlliesPermissions.CREATE_PARTY)) throw TeamExceptions.NO_PERMISSION_CREATE_PARTY.create();

        Party party = new Party(player.getUUID(), name);
        PartyApi.API.create(source.getLevel(), party);

        source.sendSuccess(() -> ModUtils.translatableWithStyle("command.odyssey_allies.party_create", party.displayName()), false);
    }
}
