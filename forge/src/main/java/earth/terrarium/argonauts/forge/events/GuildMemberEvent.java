package earth.terrarium.argonauts.forge.events;

import earth.terrarium.argonauts.common.handlers.guild.Guild;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.eventbus.api.Event;

public class GuildMemberEvent extends Event {

    private final MinecraftServer server;
    private final Guild guild;
    private final ServerPlayer player;

    public GuildMemberEvent(MinecraftServer server, Guild guild, ServerPlayer player) {
        this.server = server;
        this.guild = guild;
        this.player = player;
    }

    public MinecraftServer server() {
        return this.server;
    }

    public Guild guild() {
        return this.guild;
    }

    public ServerPlayer player() {
        return this.player;
    }

    public static class Joined extends GuildMemberEvent {
        public Joined(MinecraftServer server, Guild guild, ServerPlayer player) {
            super(server, guild, player);
        }
    }

    public static class Left extends GuildMemberEvent {
        public Left(MinecraftServer server, Guild guild, ServerPlayer player) {
            super(server, guild, player);
        }
    }
}
