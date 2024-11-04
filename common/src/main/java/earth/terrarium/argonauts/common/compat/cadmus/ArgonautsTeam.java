package earth.terrarium.argonauts.common.compat.cadmus;

import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.argonauts.api.teams.guild.Guild;
import earth.terrarium.argonauts.api.teams.guild.GuildApi;
import earth.terrarium.cadmus.api.teams.Team;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.stream.Collectors;

public class ArgonautsTeam implements Team {

    @Override
    public Optional<Component> getName(Level level, UUID id) {
        return GuildApi.API.get(level, id).map(Guild::displayName);
    }

    @Override
    public Optional<Color> getColor(Level level, UUID id) {
        return GuildApi.API.get(level, id).map(Guild::color);
    }

    @Override
    public Set<UUID> getMembers(Level level, UUID id) {
        return GuildApi.API.get(level, id).map(Guild::members)
            .map(Map::keySet).orElse(new HashSet<>());
    }

    @Override
    public boolean isMember(Level level, UUID id, Player player) {
        return GuildApi.API.get(level, id).map(guild ->
            guild.isMemberOrFakePlayer(player.getUUID()) || guild.isAllied(player.getUUID())).orElse(false);
    }

    @Override
    public Optional<UUID> getId(Player player) {
        return GuildApi.API.getPlayerGuild(player).map(Guild::id);
    }

    @Override
    public boolean canModifySettings(Player player) {
        return GuildApi.API.getPlayerGuild(player)
            .map(guild -> guild.canManageSettings(player.getUUID())).orElse(false);
    }

    @Override
    public Set<UUID> getAllTeams(MinecraftServer server) {
        return GuildApi.API.getAll(server.overworld()).stream()
            .map(Guild::id).collect(Collectors.toSet());
    }
}
