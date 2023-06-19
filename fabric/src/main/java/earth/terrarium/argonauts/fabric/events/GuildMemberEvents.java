package earth.terrarium.argonauts.fabric.events;

import earth.terrarium.argonauts.common.handlers.guild.Guild;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public final class GuildMemberEvents {

    public static final Event<Joined> JOINED = EventFactory.createArrayBacked(Joined.class, listeners -> (server, guild, player) -> {
        for (Joined listener : listeners) {
            listener.join(server, guild, player);
        }
    });

    public static final Event<Left> LEFT = EventFactory.createArrayBacked(Left.class, listeners -> (server, guild, player) -> {
        for (Left listener : listeners) {
            listener.left(server, guild, player);
        }
    });

    @FunctionalInterface
    public interface Joined {
        void join(MinecraftServer server, Guild guild, ServerPlayer player);
    }

    @FunctionalInterface
    public interface Left {
        void left(MinecraftServer server, Guild guild, UUID player);
    }

    private GuildMemberEvents() {
    }
}