package earth.terrarium.argonauts.common.compat.cadmus;

import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.teams.guild.Guild;
import earth.terrarium.argonauts.api.teams.guild.GuildApi;
import earth.terrarium.cadmus.api.teams.TeamProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.stream.Collectors;

public class ArgonautsTeam implements TeamProvider {
    public static final ResourceLocation ID = Argonauts.id("team");
    public static final ArgonautsTeam INSTANCE = new ArgonautsTeam();

    @Override
    public ResourceLocation id() {
        return ID;
    }

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
    public Set<UUID> getTeams(Player player) {
        return GuildApi.API.getPlayerGuild(player).map(guild -> Set.of(guild.id())).orElse(Set.of());
    }

    @Override
    public boolean canModifySettings(Player player, UUID uuid) {
         return GuildApi.API.get(player.level(), uuid)
            .map(guild -> guild.canManageSettings(player.getUUID())).orElse(false);
    }

    @Override
    public Set<UUID> getAllTeams(MinecraftServer server) {
        return GuildApi.API.getAll(server.overworld()).stream()
            .map(Guild::id).collect(Collectors.toSet());
    }
}
