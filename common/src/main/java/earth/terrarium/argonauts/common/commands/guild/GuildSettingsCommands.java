package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.guild.Guild;
import earth.terrarium.argonauts.common.handlers.guild.GuildHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ColorArgument;
import net.minecraft.commands.arguments.ComponentArgument;
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
                .then(color())
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
                    var hq = guild.settings().hq();
                    if (hq.isEmpty()) {
                        throw MemberException.HQ_NOT_SET;
                    }
                    hq.ifPresent(hq1 -> player.displayClientMessage(getCurrentComponent("hq", hq1.dimension().location() + ", " + hq1.pos().getX() + ", " + hq1.pos().getY() + ", " + hq1.pos().getZ()), false));
                });
                return 1;
            });
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> displayName() {
        return Commands.literal("displayName")
            .then(Commands.argument("value", ComponentArgument.textComponent())
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    CommandHelper.runAction(() -> {
                        Component pos = ComponentArgument.getComponent(context, "value");
                        pos = pos.copy().setStyle(pos.getStyle().withClickEvent(null));
                        Guild guild = getGuild(player);
                        if (!guild.members().isLeader(player.getUUID())) {
                            throw MemberException.YOU_ARE_NOT_THE_OWNER_OF_GUILD;
                        }
                        guild.settings().setDisplayName(pos);
                        player.displayClientMessage(setCurrentComponent("displayName", pos.getString()), false);
                    });
                    return 1;
                }))
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                CommandHelper.runAction(() -> {
                    Guild guild = getGuild(player);
                    player.displayClientMessage(getCurrentComponent("displayName", guild.getDisplayName().getString()), false);
                });
                return 1;
            });
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> motd() {
        return Commands.literal("motd")
            .then(Commands.argument("value", ComponentArgument.textComponent())
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    CommandHelper.runAction(() -> {
                        Component name = ComponentArgument.getComponent(context, "value");
                        name = name.copy().setStyle(name.getStyle().withClickEvent(null));
                        Guild guild = getGuild(player);
                        if (!guild.members().isLeader(player.getUUID())) {
                            throw MemberException.YOU_ARE_NOT_THE_OWNER_OF_GUILD;
                        }
                        guild.settings().setMotd(name);
                        player.displayClientMessage(setCurrentComponent("motd", name.getString()), false);
                    });
                    return 1;
                }))
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                CommandHelper.runAction(() -> {
                    Guild guild = getGuild(player);
                    player.displayClientMessage(getCurrentComponent("motd", guild.getMotd().getString()), false);
                });
                return 1;
            });
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> color() {
        return Commands.literal("color")
            .then(Commands.argument("value", ColorArgument.color())
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    CommandHelper.runAction(() -> {
                        ChatFormatting color = ColorArgument.getColor(context, "value");

                        Guild guild = getGuild(player);
                        if (!guild.members().isLeader(player.getUUID())) {
                            throw MemberException.YOU_ARE_NOT_THE_OWNER_OF_GUILD;
                        }
                        guild.settings().setColor(color);
                        player.displayClientMessage(setCurrentComponent("color", color.name()), false);
                    });
                    return 1;
                }))
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                CommandHelper.runAction(() -> {
                    Guild guild = getGuild(player);
                    player.displayClientMessage(getCurrentComponent("color", guild.getColor().name()), false);
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
