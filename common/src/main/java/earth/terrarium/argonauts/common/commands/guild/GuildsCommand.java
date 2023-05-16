package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.constants.ConstantComponents;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.guild.Guild;
import earth.terrarium.argonauts.common.handlers.guild.GuildHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class GuildsCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("guilds")
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                CommandHelper.runAction(() -> {
                    Collection<Guild> guilds = GuildHandler.getAll(player.server);
                    if (guilds.isEmpty()) throw MemberException.THERE_ARE_NO_GUILDS;
                    guilds.forEach(guild -> {
                        var name = guild.getDisplayName();
                        var owner = guild.members().getLeader().profile().getName();
                        var members = guild.members().allMembers();
                        player.displayClientMessage(Component.empty(), false);
                        player.displayClientMessage(ConstantComponents.LINE, false);
                        player.displayClientMessage(name.copy().withStyle(guild.getColor()), false);
                        player.displayClientMessage(Component.literal(guild.id().toString()).withStyle(guild.getColor()), false);
                        player.displayClientMessage(Component.empty(), false);
                        player.displayClientMessage(ConstantComponents.OWNER, false);
                        player.displayClientMessage(Component.literal(owner), false);
                        player.displayClientMessage(ConstantComponents.MEMBERS, false);
                        for (var member : members) {
                            player.displayClientMessage(Component.literal(member.profile().getName()), false);
                        }
                    });
                });
                return 1;
            })
        );
    }
}
