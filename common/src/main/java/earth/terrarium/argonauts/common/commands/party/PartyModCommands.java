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
import earth.terrarium.argonauts.common.menus.BasicContentMenuProvider;
import earth.terrarium.argonauts.common.menus.PartySettingContent;
import earth.terrarium.argonauts.common.menus.PartySettingMenu;
import it.unimi.dsi.fastutil.objects.Object2BooleanLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;

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
        return Commands.literal("tp")
            .then(Commands.argument("player", EntityArgument.player())
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    ServerPlayer target = EntityArgument.getPlayer(context, "player");
                    PartyCommandHelper.runPartyAction(() -> {
                        Party party = PartyCommandHelper.getPartyOrThrow(player, false);
                        Party otherParty = PartyCommandHelper.getPartyOrThrow(target, true);
                        if (party != otherParty) {
                            throw PartyException.NOT_IN_SAME_PARTY;
                        }
                        PartyMember member = party.getMember(player);
                        PartyMember targetMember = party.getMember(target);
                        if (!member.hasPermission(MemberPermissions.TP_MEMBERS)) {
                            throw PartyException.YOU_CANT_TP_MEMBERS;
                        }
                        if (!party.settings().has(DefaultPartySettings.PASSIVE_TP)) {
                            throw PartyException.PARTY_HAS_PASSIVE_TP_DISABLED;
                        }
                        if (!targetMember.settings().has(DefaultPartySettings.PASSIVE_TP)) {
                            throw PartyException.MEMBER_HAS_PASSIVE_TP_DISABLED;
                        }
                        player.teleportTo(
                            target.getLevel(),
                            target.getX(), target.getY(), target.getZ(),
                            target.getYRot(), target.getXRot()
                        );
                    });
                    return 1;
                }));
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> settings() {
        return Commands.literal("settings")
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                PartyCommandHelper.runPartyAction(() -> openSettingsScreen(player));
                return 1;
            });
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> warp() {
        return Commands.literal("warp")
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                Party party = PartyCommandHelper.getPartyOrThrow(player, false);
                PartyCommandHelper.runPartyAction(() -> {
                    PartyMember member = party.getMember(player);
                    if (member.hasPermission(MemberPermissions.TP_MEMBERS)) {
                        tpAllMembers(party, player);
                    } else {
                        throw PartyException.YOU_CANT_TP_MEMBERS;
                    }
                });
                return 1;
            });
    }

    private static void tpAllMembers(Party party, ServerPlayer target) {
        PlayerList list = target.server.getPlayerList();
        for (PartyMember member : party.members()) {
            if (member.profile().getId().equals(target.getUUID())) {
                continue;
            }
            ServerPlayer player = list.getPlayer(member.profile().getId());
            if (player != null) {
                player.teleportTo(target.getLevel(), target.getX(), target.getY(), target.getZ(), target.getYRot(), target.getXRot());
            }
        }
    }

    public static void openSettingsScreen(ServerPlayer player) throws PartyException {
        Party party = PartyHandler.get(player);
        if (party == null) {
            throw PartyException.YOU_ARE_NOT_IN_PARTY;
        }
        PartyMember member = party.getMember(player);
        if (!member.hasPermission(MemberPermissions.MANAGE_SETTINGS)) {
            throw PartyException.NO_PERMISSIONS;
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
            new PartySettingContent(true, settings),
            Component.literal("Party Setting"),
            PartySettingMenu::new,
            player
        );
    }
}
