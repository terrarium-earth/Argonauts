package earth.terrarium.odyssey_allies.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.odyssey_allies.api.teams.party.Party;
import earth.terrarium.odyssey_allies.api.teams.party.PartyApi;
import earth.terrarium.odyssey_allies.api.teams.settings.Setting;
import earth.terrarium.odyssey_allies.api.teams.settings.TeamSettingsApi;
import earth.terrarium.odyssey_allies.common.commands.AlliesExcepetions;
import earth.terrarium.odyssey_allies.common.utils.ModUtils;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public final class PartySettingsCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        TeamSettingsApi.API.getPartySettings().forEach((id, setting) ->
            dispatcher.register(Commands.literal("party")
                .then(Commands.literal("settings")
                    .then(Commands.literal(id)
                        .then(setting.createArgument("value")
                            .executes(context -> {
                                Setting<?> value = setting.getFromArgument("value", context);
                                set(context.getSource(), value, id);
                                return 1;
                            })
                        )
                        .executes(context -> {
                            get(context.getSource(), id);
                            return 1;
                        })
                    )
                )
            )
        );
    }

    private static void set(CommandSourceStack source, Setting<?> setting, String settingId) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Party party = PartyApi.API.getPlayerParty(player).orElse(null);
        if (party == null) throw AlliesExcepetions.NOT_IN_PARTY.create();
        if (!party.canManageSettings(player.getUUID())) throw AlliesExcepetions.NO_PERMISSION_MANAGE_SETTINGS.create();

        Setting<?> oldSettingValue = TeamSettingsApi.API.getSetting(party, settingId);
        PartyApi.API.modifySetting(source.getLevel(), party, setting, settingId);
        source.sendSuccess(() -> ModUtils.translatableWithStyle("command.odyssey_allies.setting.set", Component.translatable("setting.odyssey_allies." + settingId), oldSettingValue, setting), false);
    }

    private static void get(CommandSourceStack source, String settingId) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Party party = PartyApi.API.getPlayerParty(player).orElse(null);
        if (party == null) throw AlliesExcepetions.NOT_IN_PARTY.create();
        Setting<?> value = TeamSettingsApi.API.getSetting(party, settingId);
        source.sendSuccess(() -> ModUtils.translatableWithStyle("command.odyssey_allies.setting.get", settingId, value), false);
    }
}
