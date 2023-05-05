package earth.terrarium.argonauts.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import earth.terrarium.argonauts.common.commands.guild.GuildChatCommands;
import earth.terrarium.argonauts.common.commands.guild.GuildCommand;
import earth.terrarium.argonauts.common.commands.guild.GuildCommands;
import earth.terrarium.argonauts.common.commands.party.*;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;

public class ModCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext ctx, Commands.CommandSelection environment) {
        PartyCommand.register(dispatcher);
        PartyCommands.register(dispatcher);
        PartyLeaderCommands.register(dispatcher);
        PartyManageCommands.register(dispatcher);
        PartyModCommands.register(dispatcher);
        PartyMemberCommands.register(dispatcher);
        PartyChatCommands.register(dispatcher);

        GuildCommand.register(dispatcher);
        GuildCommands.register(dispatcher);
        GuildChatCommands.register(dispatcher);
    }
}
