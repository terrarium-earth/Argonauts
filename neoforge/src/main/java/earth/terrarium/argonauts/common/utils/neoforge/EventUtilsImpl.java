package earth.terrarium.argonauts.common.utils.neoforge;

import earth.terrarium.argonauts.api.guild.Guild;
import earth.terrarium.argonauts.neoforge.events.GuildEvent;
import earth.terrarium.argonauts.neoforge.events.GuildMemberEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.EventHooks;

public class EventUtilsImpl {
    public static boolean tpCommand(ServerPlayer player, BlockPos target) {
        return !EventHooks.onEntityTeleportCommand(player, target.getX(), target.getY(), target.getZ()).isCanceled();
    }

    public static void created(Guild guild, ServerPlayer creator) {
        NeoForge.EVENT_BUS.post(new GuildEvent.Created(guild, creator));
    }

    public static void disbanned(Guild guild) {
        NeoForge.EVENT_BUS.post(new GuildEvent.Disbanned(guild));
    }

    public static void removed(boolean forcefully, Guild guild) {
        NeoForge.EVENT_BUS.post(new GuildEvent.Removed(forcefully, guild));
    }

    public static void joined(Guild guild, ServerPlayer player) {
        NeoForge.EVENT_BUS.post(new GuildMemberEvent.Joined(player.getServer(), guild, player));
    }

    public static void left(Guild guild, ServerPlayer player) {
        NeoForge.EVENT_BUS.post(new GuildMemberEvent.Left(player.getServer(), guild, player));
    }
}
