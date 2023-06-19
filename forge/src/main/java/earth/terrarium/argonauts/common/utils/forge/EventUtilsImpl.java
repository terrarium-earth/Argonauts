package earth.terrarium.argonauts.common.utils.forge;

import earth.terrarium.argonauts.common.handlers.guild.Guild;
import earth.terrarium.argonauts.forge.events.GuildEvent;
import earth.terrarium.argonauts.forge.events.GuildMemberEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;

public class EventUtilsImpl {
    public static boolean tpCommand(ServerPlayer player, BlockPos target) {
        return !ForgeEventFactory.onEntityTeleportCommand(player, target.getX(), target.getY(), target.getZ()).isCanceled();
    }

    public static void created(Guild guild, ServerPlayer creator) {
        MinecraftForge.EVENT_BUS.post(new GuildEvent.Created(guild, creator));
    }

    public static void disbanned(Guild guild) {
        MinecraftForge.EVENT_BUS.post(new GuildEvent.Disbanned(guild));
    }

    public static void removed(boolean forcefully, Guild guild) {
        MinecraftForge.EVENT_BUS.post(new GuildEvent.Removed(forcefully, guild));
    }

    public static void joined(Guild guild, ServerPlayer player) {
        MinecraftForge.EVENT_BUS.post(new GuildMemberEvent.Joined(player.getServer(), guild, player));
    }

    public static void left(Guild guild, ServerPlayer player) {
        MinecraftForge.EVENT_BUS.post(new GuildMemberEvent.Left(player.getServer(), guild, player));
    }
}
