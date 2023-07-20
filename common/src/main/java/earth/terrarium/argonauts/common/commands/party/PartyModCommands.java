package earth.terrarium.argonauts.common.commands.party;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import earth.terrarium.argonauts.common.commands.base.BaseModCommands;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.party.members.PartyMember;
import earth.terrarium.argonauts.common.handlers.party.settings.DefaultPartySettings;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public final class PartyModCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("party")
            .then(warp())
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

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> warp() {
        return BaseModCommands.warp(
            PartyCommandHelper::getPartyOrThrow,
            MemberException.YOU_CANT_TP_MEMBERS_IN_PARTY);
    }
}
