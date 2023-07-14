package earth.terrarium.argonauts.forge.events;

import earth.terrarium.argonauts.api.guild.Guild;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Event;

public class GuildEvent extends Event {

    private final Guild guild;

    public GuildEvent(Guild guild) {
        this.guild = guild;
    }

    public Guild guild() {
        return this.guild;
    }


    public static class Created extends GuildEvent {
        private final ServerPlayer creator;

        public Created(Guild guild, ServerPlayer creator) {
            super(guild);
            this.creator = creator;
        }

        public ServerPlayer creator() {
            return this.creator;
        }
    }

    public static class Disbanned extends GuildEvent {
        public Disbanned(Guild guild) {
            super(guild);
        }
    }

    public static class Removed extends GuildEvent {
        private final boolean forcefully;

        public Removed(boolean forcefully, Guild guild) {
            super(guild);
            this.forcefully = forcefully;
        }

        public boolean forcefully() {
            return this.forcefully;
        }
    }
}
