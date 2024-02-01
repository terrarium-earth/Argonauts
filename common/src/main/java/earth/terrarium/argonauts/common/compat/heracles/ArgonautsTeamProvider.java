package earth.terrarium.argonauts.common.compat.heracles;

import com.mojang.authlib.GameProfile;
import earth.terrarium.argonauts.api.guild.Guild;
import earth.terrarium.argonauts.api.guild.GuildApi;
import earth.terrarium.argonauts.common.handlers.guild.members.GuildMember;
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
        return getTeams(player.serverLevel(), player.getUUID());
    }

    @Override
    public Stream<List<UUID>> getTeams(ServerLevel level, UUID player) {
        Guild guild = GuildApi.API.getPlayerGuild(level.getServer(), player);
        if (guild == null) return Stream.empty();
        return Stream.of(guild.members()
            .allMembers()
            .stream()
            .filter(member -> member.getState().isPermanentMember())
            .map(GuildMember::profile)
            .map(GameProfile::getId)
            .filter(uuid -> !uuid.equals(player))
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
