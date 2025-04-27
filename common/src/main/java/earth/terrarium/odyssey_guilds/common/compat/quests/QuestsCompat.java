package earth.terrarium.odyssey_guilds.common.compat.quests;

import earth.terrarium.odyssey_guilds.OdysseyGuilds;
import earth.terrarium.odyssey_guilds.api.events.OdysseyGuildsEvents;
import earth.terrarium.heracles.api.teams.TeamProviders;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

import java.util.UUID;

public class QuestsCompat {

    public static final ResourceLocation ODYSSEY_GUILDS_ID = OdysseyGuilds.id(OdysseyGuilds.MOD_ID);

    public static void init() {
        TeamProviders.register(ODYSSEY_GUILDS_ID, new GuildTeamProvider());

        OdysseyGuildsEvents.CreateGuildEvent.register((level, guild) -> {
            if (level instanceof ServerLevel serverLevel) {
                updateChanger(serverLevel, guild.id());
            }
        });

        OdysseyGuildsEvents.ModifyGuildMemberEvent.register((level, guild, player, status) -> {
            if (level instanceof ServerLevel serverLevel) {
                updateChanger(serverLevel, guild.id());
            }
        });

        OdysseyGuildsEvents.RemoveGuildMemberEvent.register((level, guild, player) -> {
            if (level instanceof ServerLevel serverLevel) {
                updateChanger(serverLevel, guild.id());
            }
        });
    }

    public static void updateChanger(ServerLevel level, UUID player) {
        GuildTeamProvider.changed(level, player);
    }
}
