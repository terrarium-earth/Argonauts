package earth.terrarium.argonauts.common.compat.heracles;

import earth.terrarium.heracles.api.teams.TeamProvider;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class ArgonautsTeamProvider implements TeamProvider {
    public static BiConsumer<ServerLevel, UUID> changer;

    @Override
    public Stream<List<UUID>> getTeams(ServerPlayer player) {
        return Stream.of();
    }

    @Override
    public Stream<List<UUID>> getTeams(ServerLevel level, UUID player) {
        return Stream.of();
    }

    @Override
    public void setupTeamChanger(BiConsumer<ServerLevel, UUID> teamChanger) {
        changer = teamChanger;
    }

    public static void changed(ServerLevel level, ServerPlayer player) {
        changer.accept(level, player.getUUID());
    }
}
