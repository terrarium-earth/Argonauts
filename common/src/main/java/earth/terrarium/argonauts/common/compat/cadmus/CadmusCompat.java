package earth.terrarium.argonauts.common.compat.cadmus;

import earth.terrarium.argonauts.api.events.ArgonautsEvents;
import earth.terrarium.cadmus.api.teams.TeamApi;
import net.minecraft.server.level.ServerLevel;

public class CadmusCompat {

    public static void init() {
        TeamApi.API.register(ArgonautsTeam.ID, ArgonautsTeam.INSTANCE);

        ArgonautsEvents.CreateGuildEvent.register((level, guild) -> {
            if (!level.isClientSide()) {
                ArgonautsTeam.INSTANCE.onCreate(level.getServer(), guild.id());
            }
        });

        ArgonautsEvents.RemoveGuildEvent.register((level, guild) -> {
            if (!level.isClientSide()) {
                ArgonautsTeam.INSTANCE.onRemove(level.getServer(), guild.id());
            }
        });

        ArgonautsEvents.GuildChangedEvent.register((level, guild) -> {
            if (!level.isClientSide()) {
                ArgonautsTeam.INSTANCE.onChange(level.getServer(), guild.id());
            }
        });

        ArgonautsEvents.ModifyGuildMemberEvent.register((level, guild, player, status) -> {
            if (level instanceof ServerLevel serverLevel) {
                ArgonautsTeam.INSTANCE.onPlayerAdded(serverLevel.getServer(), guild.id(), serverLevel.getServer().getPlayerList().getPlayer(player));
            }
        });

        ArgonautsEvents.RemoveGuildMemberEvent.register((level, guild, player) -> {
            if (level instanceof ServerLevel serverLevel) {
                ArgonautsTeam.INSTANCE.onPlayerRemoved(serverLevel.getServer(), guild.id(), serverLevel.getServer().getPlayerList().getPlayer(player));
            }
        });
    }
}
