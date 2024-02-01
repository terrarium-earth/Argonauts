package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import earth.terrarium.argonauts.api.guild.Guild;
import earth.terrarium.argonauts.api.guild.GuildApi;
import earth.terrarium.argonauts.common.commands.base.ChatCommands;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.chat.ChatHandler;
import earth.terrarium.argonauts.common.handlers.chat.ChatMessageType;
import earth.terrarium.argonauts.common.handlers.chat.MessageChannel;
import earth.terrarium.argonauts.common.network.messages.ServerboundChatWindowPacket;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public final class GuildChatCommands {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        CommandHelper.register(dispatcher, "guild", "chat", GuildChatCommands::openChatScreen);
        dispatcher.register(Commands.literal("guild").then(sendMessage()));
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

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> sendMessage() {
        return Commands.literal("chat").then(Commands.argument("message", StringArgumentType.greedyString())
            .executes(context -> {
                CommandHelper.runAction(() -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    Guild guild = GuildApi.API.get(player);
                    if (guild == null) throw MemberException.YOU_ARE_NOT_IN_GUILD;
                    String message = StringArgumentType.getString(context, "message");
                    MessageChannel channel = ChatHandler.getChannel(guild, ChatMessageType.GUILD);
                    ServerboundChatWindowPacket.sendMessage(player, guild, message, channel);
                });
                return 1;
            }));
    }
}

