package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import earth.terrarium.argonauts.api.guild.Guild;
import earth.terrarium.argonauts.api.guild.GuildApi;
import earth.terrarium.argonauts.common.commands.base.ChatCommands;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.chat.ChatMessageType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;

public final class GuildChatCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        CommandHelper.register(dispatcher, "guild", "chat", GuildChatCommands::openChatScreen);
    }

    public static void openChatScreen(ServerPlayer player) throws MemberException {
        Guild guild = GuildApi.API.get(player);
        if (guild == null) throw MemberException.YOU_ARE_NOT_IN_GUILD;

        ChatCommands.openChatScreen(player,
            guild,
            ChatMessageType.GUILD,
            ConstantComponents.GUILD_CHAT_TITLE
        );
    }
}

