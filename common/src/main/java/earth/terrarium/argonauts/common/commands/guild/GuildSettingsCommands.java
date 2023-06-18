package earth.terrarium.argonauts.common.commands.guild;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import earth.terrarium.argonauts.common.commands.base.CommandHelper;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.guild.Guild;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.ColorArgument;
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
                .then(allowFakePlayers())
            ));
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> hq() {
        return Commands.literal("hq")
            .then(Commands.argument("value", BlockPosArgument.blockPos())
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    CommandHelper.runAction(() -> {
                        BlockPos pos = BlockPosArgument.getBlockPos(context, "value");
                        Guild guild = GuildCommandHelper.getGuildOrThrow(player, false);
                        if (!guild.members().isLeader(player.getUUID())) {
                            throw MemberException.YOU_ARE_NOT_THE_OWNER_OF_GUILD;
                        }
                        guild.settings().setHq(GlobalPos.of(player.level().dimension(), pos));
                        player.displayClientMessage(setCurrentComponent("hq", player.level().dimension().location() + ", " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ()), false);
                    });
                    return 1;
                }))
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                CommandHelper.runAction(() -> {
                    Guild guild = GuildCommandHelper.getGuildOrThrow(player, false);
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
            .then(Commands.argument("value", StringArgumentType.greedyString())
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    CommandHelper.runAction(() -> {
                        String displayName = ModUtils.formatTextColors(StringArgumentType.getString(context, "value"));
                        Guild guild = GuildCommandHelper.getGuildOrThrow(player, false);
                        if (!guild.members().isLeader(player.getUUID())) {
                            throw MemberException.YOU_ARE_NOT_THE_OWNER_OF_GUILD;
                        }
                        guild.settings().setDisplayName(Component.literal(displayName));
                        player.displayClientMessage(setCurrentComponent("displayName", displayName), false);
                    });
                    return 1;
                }))
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                CommandHelper.runAction(() -> {
                    Guild guild = GuildCommandHelper.getGuildOrThrow(player, false);
                    player.displayClientMessage(getCurrentComponent("displayName", guild.displayName()), false);
                });
                return 1;
            });
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> motd() {
        return Commands.literal("motd")
            .then(Commands.argument("value", StringArgumentType.greedyString())
                .executes(context -> {
                    ServerPlayer player = context.getSource().getPlayerOrException();
                    CommandHelper.runAction(() -> {
                        String name = ModUtils.formatTextColors(StringArgumentType.getString(context, "value")).replace("\\n", "\n");
                        Guild guild = GuildCommandHelper.getGuildOrThrow(player, false);
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
                    Guild guild = GuildCommandHelper.getGuildOrThrow(player, false);
                    player.displayClientMessage(getCurrentComponent("motd", guild.motd()), false);
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

                        Guild guild = GuildCommandHelper.getGuildOrThrow(player, false);
                        if (!guild.members().isLeader(player.getUUID())) {
                            throw MemberException.YOU_ARE_NOT_THE_OWNER_OF_GUILD;
                        }
                        guild.settings().setColor(color);
                        player.displayClientMessage(setCurrentComponent("color", guild.color().name().toLowerCase()), false);
                    });
                    return 1;
                }))
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                CommandHelper.runAction(() -> {
                    Guild guild = GuildCommandHelper.getGuildOrThrow(player, false);
                    player.displayClientMessage(getCurrentComponent("color", guild.color().name().toLowerCase()), false);
                });
                return 1;
            });
    }

    private static ArgumentBuilder<CommandSourceStack, LiteralArgumentBuilder<CommandSourceStack>> allowFakePlayers() {
        return Commands.literal("allowFakePlayers")
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayerOrException();
                CommandHelper.runAction(() -> {
                    Guild guild = GuildCommandHelper.getGuildOrThrow(player, false);
                    guild.settings().setAllowFakePlayers(!guild.settings().allowFakePlayers());
                    player.displayClientMessage(getCurrentComponent("allowFakePlayers", String.valueOf(guild.settings().allowFakePlayers())), false);
                });
                return 1;
            });
    }

    private static Component getCurrentComponent(String command, Object value) {
        return Component.translatable("text.argonauts.guild_settings.current", command, value);
    }

    private static Component setCurrentComponent(String command, Object value) {
        return Component.translatable("text.argonauts.guild_settings.set", command, value);
    }
}
