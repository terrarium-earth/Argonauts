package earth.terrarium.argonauts.common.compat.quests;

import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.events.ArgonautsEvents;
import earth.terrarium.heracles.api.teams.TeamProviders;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

import java.util.UUID;

public class QuestsCompat {

    public static final ResourceLocation ODYSSEY_ALLIES_ID = Argonauts.id(Argonauts.MOD_ID);

    public static void init() {
        TeamProviders.register(ODYSSEY_ALLIES_ID, new GuildQuestTeam());

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
        GuildQuestTeam.changed(level, player);
    }
}
