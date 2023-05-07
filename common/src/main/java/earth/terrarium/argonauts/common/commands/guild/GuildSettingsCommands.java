package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.guild.Guild;
import earth.terrarium.argonauts.common.handlers.guild.GuildHandler;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public final class GuildSettingsCommands {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("guild")
            .then(Commands.literal("settings")
                .then(hq())
                .then(displayName())
                .then(motd())
            ));
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> hq() {
        return Commands.literal("hq")
            .then(Commands.argument("value", BlockPosArgument.blockPos())
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    CommandHelper.runAction(() -> {
                        BlockPos pos = BlockPosArgument.getBlockPos(context, "value");
                        Guild guild = getGuild(player);
                        if (!guild.members().isLeader(player.getUUID())) {
                            throw MemberException.YOU_ARE_NOT_THE_OWNER_OF_GUILD;
                        }
                        guild.settings().setHq(GlobalPos.of(player.level.dimension(), pos));
                        player.displayClientMessage(setCurrentComponent("hq", player.level.dimension().location() + ", " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()), false);
                    });
                    return 1;
                }))
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                CommandHelper.runAction(() -> {
                    Guild guild = getGuild(player);
                    GlobalPos hq = guild.settings().hq();
                    player.displayClientMessage(getCurrentComponent("hq", hq.dimension().location() + ", " + hq.pos().getX() + ", " + hq.pos().getY() + ", " + hq.pos().getZ()), false);
                });
                return 1;
            });
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> displayName() {
        return Commands.literal("displayName")
            .then(Commands.argument("value", StringArgumentType.string())
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    CommandHelper.runAction(() -> {
                        String pos = StringArgumentType.getString(context, "value");
                        Guild guild = getGuild(player);
                        if (!guild.members().isLeader(player.getUUID())) {
                            throw MemberException.YOU_ARE_NOT_THE_OWNER_OF_GUILD;
                        }
                        guild.settings().setDisplayName(Component.nullToEmpty(pos));
                        player.displayClientMessage(setCurrentComponent("displayName", pos), false);
                    });
                    return 1;
                }))
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                CommandHelper.runAction(() -> {
                    Guild guild = getGuild(player);
                    player.displayClientMessage(getCurrentComponent("displayName", guild.settings().displayName().getString()), false);
                });
                return 1;
            });
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> motd() {
        return Commands.literal("motd")
            .then(Commands.argument("value", StringArgumentType.string())
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    CommandHelper.runAction(() -> {
                        String name = StringArgumentType.getString(context, "value");
                        name = name.replace("&&", "§").replace("\\n", "\n");

                        Guild guild = getGuild(player);
                        if (!guild.members().isLeader(player.getUUID())) {
                            throw MemberException.YOU_ARE_NOT_THE_OWNER_OF_GUILD;
                        }
                        guild.settings().setMotd(Component.literal(name));
                        player.displayClientMessage(setCurrentComponent("motd", name), false);
                    });
                    return 1;
                }))
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                CommandHelper.runAction(() -> {
                    Guild guild = getGuild(player);
                    player.displayClientMessage(getCurrentComponent("motd", guild.settings().motd().getString()), false);
                });
                return 1;
            });
    }

    private static Guild getGuild(ServerPlayer player) throws MemberException {
        Guild guild = GuildHandler.get(player);
        if (guild == null) {
            throw MemberException.YOU_ARE_NOT_IN_GUILD;
        }
        return guild;
    }

    private static Component getCurrentComponent(String command, String value) {
        return Component.translatable("text.argonauts.guild_settings.current", command, value);
    }

    private static Component setCurrentComponent(String command, String value) {
        return Component.translatable("text.argonauts.guild_settings.set", command, value);
    }
}
