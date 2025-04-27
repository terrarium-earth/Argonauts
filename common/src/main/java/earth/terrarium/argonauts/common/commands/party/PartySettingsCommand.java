package earth.terrarium.argonauts.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import earth.terrarium.argonauts.api.teams.party.Party;
import earth.terrarium.argonauts.api.teams.party.PartyApi;
import earth.terrarium.argonauts.api.teams.settings.Setting;
import earth.terrarium.argonauts.api.teams.settings.TeamSettingsApi;
import earth.terrarium.argonauts.common.commands.TeamExceptions;
import earth.terrarium.argonauts.common.utils.ModUtils;
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
        if (party == null) throw TeamExceptions.NOT_IN_PARTY.create();
        if (!party.canManageSettings(player.getUUID())) throw TeamExceptions.NO_PERMISSION_MANAGE_SETTINGS.create();

        Setting<?> oldSettingValue = TeamSettingsApi.API.getSetting(party, settingId);
        PartyApi.API.modifySetting(source.getLevel(), party, setting, settingId);
        source.sendSuccess(() -> ModUtils.translatableWithStyle("command.argonauts.setting.set", Component.translatable("setting.argonauts." + settingId), oldSettingValue, setting), false);
    }

    private static void get(CommandSourceStack source, String settingId) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        Party party = PartyApi.API.getPlayerParty(player).orElse(null);
        if (party == null) throw TeamExceptions.NOT_IN_PARTY.create();
        Setting<?> value = TeamSettingsApi.API.getSetting(party, settingId);
        source.sendSuccess(() -> ModUtils.translatableWithStyle("command.argonauts.setting.get", settingId, value), false);
    }
}
