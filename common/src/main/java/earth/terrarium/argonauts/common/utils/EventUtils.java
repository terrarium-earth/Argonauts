package earth.terrarium.argonauts.common.utils;

import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.argonauts.api.guild.Guild;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;

public final class EventUtils {

    /**
     * @return false if the teleport command should be cancelled.
     */
    @ExpectPlatform
    public static boolean tpCommand(ServerPlayer player, BlockPos target) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static void created(Guild guild, ServerPlayer creator) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static void disbanned(Guild guild) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static void removed(boolean forcefully, Guild guild) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static void joined(Guild guild, ServerPlayer player) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    public static void left(Guild guild, ServerPlayer player) {
        throw new NotImplementedException();
    }
}
