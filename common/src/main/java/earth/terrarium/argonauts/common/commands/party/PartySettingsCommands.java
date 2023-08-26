package earth.terrarium.argonauts.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.teamresourceful.resourcefullib.common.utils.CommonUtils;
import earth.terrarium.argonauts.api.party.Party;
import earth.terrarium.argonauts.api.party.PartyApi;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.party.members.PartyMember;
import earth.terrarium.argonauts.common.handlers.party.settings.DefaultPartySettings;
import earth.terrarium.argonauts.common.menus.party.PartySettingsContent;
import earth.terrarium.argonauts.common.network.NetworkHandler;
import earth.terrarium.argonauts.common.network.messages.ClientboundOpenPartySettingsMenuPacket;
import it.unimi.dsi.fastutil.objects.Object2BooleanLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.HashSet;
import java.util.Set;

public class PartySettingsCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("party")
            .then(settings())
            .then(Commands.literal("settings")
                .then(publicParty())
                .then(friendlyFire())
                .then(passiveTeleport())
                .then(list()))
        );
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> publicParty() {
        return Commands.literal("publicParty")
            .then(Commands.argument("value", BoolArgumentType.bool())
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    CommandHelper.runAction(() -> {
                        boolean publicParty = BoolArgumentType.getBool(context, "value");
                        Party party = PartyCommandHelper.getPartyOrThrow(player, false);
                        PartyMember member = party.getMember(player);
                        if (!member.hasPermission(MemberPermissions.MANAGE_SETTINGS)) {
                            throw MemberException.NO_PERMISSIONS;
                        }

                        party.settings().set(DefaultPartySettings.PUBLIC, publicParty);
                        player.displayClientMessage(setCurrentComponent("publicParty", publicParty), false);
                    });
                    return 1;
                }))
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                CommandHelper.runAction(() -> {
                    Party party = PartyCommandHelper.getPartyOrThrow(player, false);
                    boolean publicParty = party.settings().settings().contains(DefaultPartySettings.PUBLIC);
                    player.displayClientMessage(getCurrentComponent("publicParty", publicParty), false);
                });
                return 1;
            });
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> friendlyFire() {
        return Commands.literal("friendlyFire")
            .then(Commands.argument("value", BoolArgumentType.bool())
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    CommandHelper.runAction(() -> {
                        boolean friendlyFire = BoolArgumentType.getBool(context, "value");
                        Party party = PartyCommandHelper.getPartyOrThrow(player, false);
                        PartyMember member = party.getMember(player);
                        if (!member.hasPermission(MemberPermissions.MANAGE_SETTINGS)) {
                            throw MemberException.NO_PERMISSIONS;
                        }

                        party.settings().set(DefaultPartySettings.FRIENDLY_FIRE, friendlyFire);
                        player.displayClientMessage(setCurrentComponent("friendlyFire", friendlyFire), false);
                    });
                    return 1;
                }))
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                CommandHelper.runAction(() -> {
                    Party party = PartyCommandHelper.getPartyOrThrow(player, false);
                    boolean friendlyFire = party.settings().settings().contains(DefaultPartySettings.FRIENDLY_FIRE);
                    player.displayClientMessage(getCurrentComponent("friendlyFire", friendlyFire), false);
                });
                return 1;
            });
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> passiveTeleport() {
        return Commands.literal("passiveTeleport")
            .then(Commands.argument("value", BoolArgumentType.bool())
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    CommandHelper.runAction(() -> {
                        boolean passiveTeleport = BoolArgumentType.getBool(context, "value");
                        Party party = PartyCommandHelper.getPartyOrThrow(player, false);
                        PartyMember member = party.getMember(player);
                        if (!member.hasPermission(MemberPermissions.MANAGE_SETTINGS)) {
                            throw MemberException.NO_PERMISSIONS;
                        }

                        party.settings().set(DefaultPartySettings.PASSIVE_TP, passiveTeleport);
                        player.displayClientMessage(setCurrentComponent("passiveTeleport", passiveTeleport), false);
                    });
                    return 1;
                }))
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                CommandHelper.runAction(() -> {
                    Party party = PartyCommandHelper.getPartyOrThrow(player, false);
                    boolean passiveTeleport = party.settings().settings().contains(DefaultPartySettings.PASSIVE_TP);
                    player.displayClientMessage(getCurrentComponent("passiveTeleport", passiveTeleport), false);
                });
                return 1;
            });
    }


    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> list() {
        return Commands.literal("list")
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                CommandHelper.runAction(() -> openSettingsScreen(player));
                return 1;
            });
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> settings() {
        return Commands.literal("settings")
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                CommandHelper.runAction(() -> openSettingsScreen(player));
                return 1;
            });
    }

    public static void openSettingsScreen(ServerPlayer player) throws MemberException {
        if (!NetworkHandler.CHANNEL.canSendPlayerPackets(player)) throw MemberException.NOT_INSTALLED_ON_CLIENT;

        Party party = PartyApi.API.get(player);
        if (party == null) throw MemberException.YOU_ARE_NOT_IN_PARTY;

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

        NetworkHandler.CHANNEL.sendToPlayer(new ClientboundOpenPartySettingsMenuPacket(
            new PartySettingsContent(true, settings),
            ConstantComponents.PARTY_SETTING_TITLE), player);
    }

    private static Component getCurrentComponent(String command, Object value) {
        return CommonUtils.serverTranslatable("text.argonauts.settings.current", command, value);
    }

    private static Component setCurrentComponent(String command, Object value) {
        return CommonUtils.serverTranslatable("text.argonauts.settings.set", command, value);
    }
}
