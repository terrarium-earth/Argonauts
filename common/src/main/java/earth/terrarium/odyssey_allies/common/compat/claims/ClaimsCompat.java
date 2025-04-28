package earth.terrarium.odyssey_allies.common.compat.claims;

import earth.terrarium.odyssey_allies.api.events.AlliesEvents;
import earth.terrarium.cadmus.api.teams.TeamApi;
import net.minecraft.server.level.ServerLevel;

public class ClaimsCompat {

    public static void init() {
        TeamApi.API.register(GuildClaimTeam.ID, GuildClaimTeam.INSTANCE);

        AlliesEvents.CreateGuildEvent.register((level, guild) -> {
            if (!level.isClientSide()) {
                GuildClaimTeam.INSTANCE.onCreate(level.getServer(), guild.id());
            }
        });

        AlliesEvents.RemoveGuildEvent.register((level, guild) -> {
            if (!level.isClientSide()) {
                GuildClaimTeam.INSTANCE.onRemove(level.getServer(), guild.id());
            }
        });

        AlliesEvents.GuildChangedEvent.register((level, guild) -> {
            if (!level.isClientSide()) {
                GuildClaimTeam.INSTANCE.onChange(level.getServer(), guild.id());
            }
        });

        AlliesEvents.ModifyGuildMemberEvent.register((level, guild, player, status) -> {
            if (level instanceof ServerLevel serverLevel) {
                GuildClaimTeam.INSTANCE.onPlayerAdded(serverLevel.getServer(), guild.id(), serverLevel.getServer().getPlayerList().getPlayer(player));
            }
        });

        AlliesEvents.RemoveGuildMemberEvent.register((level, guild, player) -> {
            if (level instanceof ServerLevel serverLevel) {
                GuildClaimTeam.INSTANCE.onPlayerRemoved(serverLevel.getServer(), guild.id(), serverLevel.getServer().getPlayerList().getPlayer(player));
            }
        });
    }
}
