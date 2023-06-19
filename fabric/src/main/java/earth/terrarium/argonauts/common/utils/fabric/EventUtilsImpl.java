package earth.terrarium.argonauts.common.utils.fabric;

import earth.terrarium.argonauts.common.handlers.guild.Guild;
import earth.terrarium.argonauts.fabric.events.GuildEvents;
import earth.terrarium.argonauts.fabric.events.GuildMemberEvents;
import earth.terrarium.argonauts.fabric.events.PlayerTeleportEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

public class EventUtilsImpl {

    public static boolean tpCommand(ServerPlayer player, BlockPos target) {
        return PlayerTeleportEvents.COMMAND.invoker().check(player, target.getX(), target.getY(), target.getZ());
    }

    public static void created(Guild guild, ServerPlayer creator) {
        GuildEvents.CREATED.invoker().create(creator, guild);
    }

    public static void disbanned(Guild guild) {
        GuildEvents.DISBANDED.invoker().disband(guild);
    }

    public static void removed(boolean forcefully, Guild guild) {
        GuildEvents.REMOVED.invoker().remove(forcefully, guild);
    }

    public static void joined(Guild guild, ServerPlayer player) {
        GuildMemberEvents.JOINED.invoker().join(player.getServer(), guild, player);
    }

    public static void left(Guild guild, ServerPlayer player) {
        GuildMemberEvents.LEFT.invoker().left(player.getServer(), guild, player.getUUID());
    }
}
