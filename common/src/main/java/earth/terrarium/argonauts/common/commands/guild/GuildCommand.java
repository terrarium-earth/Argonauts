package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.handlers.guild.GuildHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public final class GuildCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("guild")
            .then(Commands.literal("create")
                .then(Commands.argument("name", ComponentArgument.textComponent())
                    .executes(context -> {
                        ServerPlayer player = context.getSource().getPlayerOrException();
                        Component name = ComponentArgument.getComponent(context, "name");
                        CommandHelper.runAction(() -> GuildHandler.createGuild(player, name.copy().setStyle(name.getStyle().withClickEvent(null))));
                        return 1;
                    }))
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    Component name = Component.translatable("text.argonauts.guild_name", player.getName().getString());
                    CommandHelper.runAction(() -> GuildHandler.createGuild(player, name));
                    return 1;
                })
            ));
    }
}

