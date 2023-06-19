package earth.terrarium.argonauts.fabric.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;

public final class PlayerTeleportEvents {


    public static final Event<Command> COMMAND = EventFactory.createArrayBacked(Command.class, listeners -> (player, x, y, z) -> {
        for (Command listener : listeners) {
            if (!listener.check(player, x, y, z)) {
                return false;
            }
        }
        return true;
    });

    @FunctionalInterface
    public interface Command {

        /**
         * @return false if the teleport command should be cancelled.
         */
        boolean check(Player player, double x, double y, double z);
    }

    private PlayerTeleportEvents() {
    }
}