package earth.terrarium.argonauts.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import earth.terrarium.argonauts.common.handlers.party.Party;
import earth.terrarium.argonauts.common.handlers.party.PartyException;
import earth.terrarium.argonauts.common.handlers.party.PartyHandler;
import earth.terrarium.argonauts.common.handlers.party.members.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.party.members.PartyMember;
import earth.terrarium.argonauts.common.handlers.party.settings.DefaultPartySettings;
import earth.terrarium.argonauts.common.menus.*;
import it.unimi.dsi.fastutil.objects.Object2BooleanLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Set;

public final class PartyMemberCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("party")
            .then(Commands.literal("member")
                .then(list())
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    PartyCommandHelper.runPartyAction(() -> openMemberScreen(player));
                    return 1;
                })
            ));
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> list() {
        return Commands.literal("list")
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                PartyCommandHelper.runPartyAction(() -> openMembersScreen(player, -1));
                return 1;
            });
    }

    public static void openMembersScreen(ServerPlayer player, int selected) throws PartyException {
        Party party = PartyHandler.get(player);
        if (party == null) {
            throw PartyException.YOU_ARE_NOT_IN_PARTY;
        }
        PartyMember member = party.getMember(player);
        BasicContentMenuProvider.open(
            new PartyMembersContent(party.id(), selected, party.members().allMembers(), member.hasPermission(MemberPermissions.MANAGE_MEMBERS), member.hasPermission(MemberPermissions.MANAGE_PERMISSIONS)),
            Component.literal("Party Members"),
            PartyMembersMenu::new,
            player
        );
    }

    public static void openMemberScreen(ServerPlayer player) throws PartyException {
        Party party = PartyHandler.get(player);
        if (party == null) {
            throw PartyException.YOU_ARE_NOT_IN_PARTY;
        }
        PartyMember member = party.getMember(player);
        Object2BooleanMap<String> settings = new Object2BooleanLinkedOpenHashMap<>();
        Set<String> oldSettings = new HashSet<>(member.settings().settings());
        for (String setting : DefaultPartySettings.MEMBER_SETTINGS) {
            settings.put(setting, oldSettings.contains(setting));
            oldSettings.remove(setting);
        }
        for (String setting : oldSettings) {
            settings.put(setting, false);
        }
        BasicContentMenuProvider.open(
            new PartySettingContent(false, settings),
            Component.literal("Member Setting"),
            PartySettingMenu::new,
            player
        );
    }
}
