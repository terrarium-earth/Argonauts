package earth.terrarium.odyssey_allies.common.compat.quests;

import earth.terrarium.odyssey_allies.OdysseyAllies;
import earth.terrarium.odyssey_allies.api.events.AlliesEvents;
import earth.terrarium.heracles.api.teams.TeamProviders;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

import java.util.UUID;

public class QuestsCompat {

    public static final ResourceLocation ODYSSEY_ALLIES_ID = OdysseyAllies.id(OdysseyAllies.MOD_ID);

    public static void init() {
        TeamProviders.register(ODYSSEY_ALLIES_ID, new GuildQuestTeam());

        AlliesEvents.CreateGuildEvent.register((level, guild) -> {
            if (level instanceof ServerLevel serverLevel) {
                updateChanger(serverLevel, guild.id());
            }
        });

        AlliesEvents.ModifyGuildMemberEvent.register((level, guild, player, status) -> {
            if (level instanceof ServerLevel serverLevel) {
                updateChanger(serverLevel, guild.id());
            }
        });

        AlliesEvents.RemoveGuildMemberEvent.register((level, guild, player) -> {
            if (level instanceof ServerLevel serverLevel) {
                updateChanger(serverLevel, guild.id());
            }
        });
    }

    public static void updateChanger(ServerLevel level, UUID player) {
        GuildQuestTeam.changed(level, player);
    }
}
