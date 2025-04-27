package earth.terrarium.odyssey_guilds.common.compat.heracles;

import earth.terrarium.odyssey_guilds.OdysseyGuilds;
import earth.terrarium.odyssey_guilds.api.events.OdysseyGuildsEvents;
import earth.terrarium.heracles.api.teams.TeamProviders;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

import java.util.UUID;

public class HeraclesCompat {

    public static final ResourceLocation ARGONAUTS_ID = OdysseyGuilds.id(OdysseyGuilds.MOD_ID);

    public static void init() {
        TeamProviders.register(ARGONAUTS_ID, new ArgonautsTeamProvider());

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
        ArgonautsTeamProvider.changed(level, player);
    }
}
