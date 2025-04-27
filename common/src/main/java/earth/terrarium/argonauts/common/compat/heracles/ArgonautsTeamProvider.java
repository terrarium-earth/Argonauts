package earth.terrarium.argonauts.common.compat.heracles;

import earth.terrarium.argonauts.api.teams.guild.Guild;
import earth.terrarium.argonauts.api.teams.guild.GuildApi;
import earth.terrarium.heracles.api.teams.TeamProvider;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class ArgonautsTeamProvider implements TeamProvider {

    public static BiConsumer<ServerLevel, UUID> changer;

    @Override
    public Stream<List<UUID>> getTeams(ServerPlayer player) {
        return getTeams(player.serverLevel(), player.getUUID());
    }

    @Override
    public Stream<List<UUID>> getTeams(ServerLevel level, UUID playerId) {
        Guild guild = GuildApi.API.getPlayerGuild(level, playerId).orElse(null);
        if (guild == null) return Stream.empty();
        return Stream.of(guild.members()
            .entrySet()
            .stream()
            .filter(member -> member.getValue().status().isMember())
            .map(Map.Entry::getKey)
            .filter(id -> !id.equals(playerId))
            .toList());
    }

    @Override
    public void setupTeamChanger(BiConsumer<ServerLevel, UUID> teamChanger) {
        changer = teamChanger;
    }

    public static void changed(ServerLevel level, UUID player) {
        changer.accept(level, player);
    }
}
