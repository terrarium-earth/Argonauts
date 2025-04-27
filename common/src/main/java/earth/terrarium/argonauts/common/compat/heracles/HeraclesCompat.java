package earth.terrarium.argonauts.common.compat.heracles;

import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.events.ArgonautsEvents;
import earth.terrarium.heracles.api.teams.TeamProviders;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

import java.util.UUID;

public class HeraclesCompat {

    public static final ResourceLocation ARGONAUTS_ID = Argonauts.id(Argonauts.MOD_ID);

    public static void init() {
        TeamProviders.register(ARGONAUTS_ID, new ArgonautsTeamProvider());

        ArgonautsEvents.CreateGuildEvent.register((level, guild) -> {
            if (level instanceof ServerLevel serverLevel) {
                updateChanger(serverLevel, guild.id());
            }
        });

        ArgonautsEvents.ModifyGuildMemberEvent.register((level, guild, player, status) -> {
            if (level instanceof ServerLevel serverLevel) {
                updateChanger(serverLevel, guild.id());
            }
        });

        ArgonautsEvents.RemoveGuildMemberEvent.register((level, guild, player) -> {
            if (level instanceof ServerLevel serverLevel) {
                updateChanger(serverLevel, guild.id());
            }
        });
    }

    public static void updateChanger(ServerLevel level, UUID player) {
        ArgonautsTeamProvider.changed(level, player);
    }
}
