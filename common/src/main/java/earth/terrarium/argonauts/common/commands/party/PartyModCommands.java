package earth.terrarium.argonauts.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import earth.terrarium.argonauts.api.party.PartyApi;
import earth.terrarium.argonauts.common.commands.base.BaseModCommands;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.party.Party;
import earth.terrarium.argonauts.common.handlers.party.members.PartyMember;
import earth.terrarium.argonauts.common.handlers.party.settings.DefaultPartySettings;
import earth.terrarium.argonauts.common.menus.BasicContentMenuProvider;
import earth.terrarium.argonauts.common.menus.party.PartySettingsContent;
import earth.terrarium.argonauts.common.menus.party.PartySettingsMenu;
import it.unimi.dsi.fastutil.objects.Object2BooleanLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Set;

public final class PartyModCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("party")
            .then(warp())
            .then(settings())
            .then(tp())
        );
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> tp() {
        return BaseModCommands.tp(
            PartyCommandHelper::getPartyOrThrow,
            MemberException.NOT_IN_SAME_PARTY,
            MemberException.YOU_CANT_TP_MEMBERS_IN_PARTY,
            (group, targetMember) -> {
                if (!group.settings().has(DefaultPartySettings.PASSIVE_TP)) {
                    throw MemberException.PARTY_HAS_PASSIVE_TP_DISABLED;
                }
                if (!((PartyMember) targetMember).settings().has(DefaultPartySettings.PASSIVE_TP)) {
                    throw MemberException.MEMBER_HAS_PASSIVE_TP_DISABLED;
                }
            }
        );
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> settings() {
        return Commands.literal("settings")
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                CommandHelper.runAction(() -> openSettingsScreen(player));
                return 1;
            });
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> warp() {
        return BaseModCommands.warp(
            PartyCommandHelper::getPartyOrThrow,
            MemberException.YOU_CANT_TP_MEMBERS_IN_PARTY);
    }

    public static void openSettingsScreen(ServerPlayer player) throws MemberException {
        Party party = PartyApi.API.get(player);
        if (party == null) {
            throw MemberException.YOU_ARE_NOT_IN_PARTY;
        }
        PartyMember member = party.getMember(player);
        if (!member.hasPermission(MemberPermissions.MANAGE_SETTINGS)) {
            throw MemberException.NO_PERMISSIONS;
        }
        Object2BooleanMap<String> settings = new Object2BooleanLinkedOpenHashMap<>();
        Set<String> oldSettings = new HashSet<>(party.settings().settings());
        for (String setting : DefaultPartySettings.PARTY_SETTINGS) {
            settings.put(setting, oldSettings.contains(setting));
            oldSettings.remove(setting);
        }
        for (String setting : oldSettings) {
            settings.put(setting, false);
        }
        BasicContentMenuProvider.open(
            new PartySettingsContent(true, settings),
            ConstantComponents.PARTY_SETTING_TITLE,
            PartySettingsMenu::new,
            player
        );
    }
}
