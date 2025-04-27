package earth.terrarium.argonauts.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import earth.terrarium.argonauts.common.commands.guild.*;
import earth.terrarium.argonauts.common.commands.party.*;
import net.minecraft.commands.CommandSourceStack;

public class ArgonautsCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        GuildCreateCommand.register(dispatcher);
        GuildDisbandCommand.register(dispatcher);
        GuildSettingsCommand.register(dispatcher);
        GuildChatCommand.register(dispatcher);
        GuildAllyCommands.register(dispatcher);
        GuildTransferCommand.register(dispatcher);
        GuildInviteCommand.register(dispatcher);
        GuildKickCommand.register(dispatcher);
        GuildJoinCommand.register(dispatcher);
        GuildLeaveCommand.register(dispatcher);
        GuildMemberCommands.register(dispatcher);
        GuildPermissionCommands.register(dispatcher);
        GuildTpCommand.register(dispatcher);
        GuildHeadquartersCommand.register(dispatcher);
        GuildFakePlayerCommands.register(dispatcher);
        GuildAdminCommands.register(dispatcher);

        PartyCreateCommand.register(dispatcher);
        PartyDisbandCommand.register(dispatcher);
        PartySettingsCommand.register(dispatcher);
        PartyChatCommand.register(dispatcher);
        PartyTransferCommand.register(dispatcher);
        PartyInviteCommand.register(dispatcher);
        PartyKickCommand.register(dispatcher);
        PartyJoinCommand.register(dispatcher);
        PartyLeaveCommand.register(dispatcher);
        PartyMemberCommands.register(dispatcher);
        PartyPermissionCommands.register(dispatcher);
        PartyTpCommand.register(dispatcher);
        PartyWarpCommand.register(dispatcher);
    }
}
