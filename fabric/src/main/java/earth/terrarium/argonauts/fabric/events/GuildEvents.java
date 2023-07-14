package earth.terrarium.argonauts.fabric.events;

import earth.terrarium.argonauts.api.guild.Guild;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.server.level.ServerPlayer;

public final class GuildEvents {


    public static final Event<Created> CREATED = EventFactory.createArrayBacked(Created.class, listeners -> (player, guild) -> {
        for (Created listener : listeners) {
            listener.create(player, guild);
        }
    });

    public static final Event<Disbanded> DISBANDED = EventFactory.createArrayBacked(Disbanded.class, listeners -> (guild) -> {
        for (Disbanded listener : listeners) {
            listener.disband(guild);
        }
    });

    public static final Event<Removed> REMOVED = EventFactory.createArrayBacked(Removed.class, listeners -> (forcefully, guild) -> {
        for (Removed listener : listeners) {
            listener.remove(forcefully, guild);
        }
    });

    @FunctionalInterface
    public interface Created {
        void create(ServerPlayer player, Guild guild);
    }

    @FunctionalInterface
    public interface Disbanded {
        void disband(Guild guild);
    }

    @FunctionalInterface
    public interface Removed {
        void remove(boolean forcefully, Guild guild);
    }

    private GuildEvents() {
    }
}