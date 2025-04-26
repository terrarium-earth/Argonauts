package earth.terrarium.odyssey_guilds.common.compat.cadmus;

import com.mojang.authlib.GameProfile;
import com.teamresourceful.resourcefullib.common.color.Color;
import earth.terrarium.odyssey_guilds.OdysseyGuilds;
import earth.terrarium.odyssey_guilds.api.teams.guild.Guild;
import earth.terrarium.odyssey_guilds.api.teams.guild.GuildApi;
import earth.terrarium.cadmus.api.teams.TeamProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.stream.Collectors;

public class GuildClaimTeam implements TeamProvider {
    public static final ResourceLocation ID = OdysseyGuilds.id("team");
    public static final GuildClaimTeam INSTANCE = new GuildClaimTeam();

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
    public boolean isMember(Level level, UUID id, GameProfile player) {
        return GuildApi.API.get(level, id).map(guild ->
            guild.isMemberOrFakePlayer(player.getId()) || guild.isAllied(player.getId())).orElse(false);
    }

    @Override
    public Set<UUID> getTeams(Level level, GameProfile gameProfile) {
        return GuildApi.API.getPlayerGuild(level, gameProfile.getId()).map(guild -> Set.of(guild.id())).orElse(Set.of());
    }

    @Override
    public boolean canModifySettings(Level level, UUID teamId, GameProfile player) {
         return GuildApi.API.get(level, teamId)
            .map(guild -> guild.canManageSettings(player.getId())).orElse(false);
    }

    @Override
    public Set<UUID> getAllTeams(MinecraftServer server) {
        return GuildApi.API.getAll(server.overworld()).stream()
            .map(Guild::id).collect(Collectors.toSet());
    }
}
