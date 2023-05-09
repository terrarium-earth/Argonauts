package earth.terrarium.argonauts.common.compat.cadmus;

import com.mojang.authlib.GameProfile;
import earth.terrarium.argonauts.common.handlers.base.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.guild.Guild;
import earth.terrarium.argonauts.common.handlers.guild.GuildHandler;
import earth.terrarium.argonauts.common.handlers.guild.members.GuildMember;
import earth.terrarium.argonauts.common.handlers.party.Party;
import earth.terrarium.argonauts.common.handlers.party.PartyHandler;
import earth.terrarium.cadmus.api.claims.InteractionType;
import earth.terrarium.cadmus.api.teams.TeamProvider;
import earth.terrarium.cadmus.common.claims.ClaimInfo;
import earth.terrarium.cadmus.common.claims.ClaimSaveData;
import earth.terrarium.cadmus.common.claims.ClaimType;
import earth.terrarium.cadmus.common.teams.Team;
import earth.terrarium.cadmus.common.teams.TeamSaveData;
import net.minecraft.Optionull;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class ArgnonautsTeamProvider implements TeamProvider {
    @Override
    public Set<GameProfile> getTeamMembers(String id, MinecraftServer server) {
        Guild guild = GuildHandler.get(server, UUID.fromString(id));
        Set<GameProfile> profiles = new HashSet<>();
        if (guild == null) return profiles;
        for (GuildMember player : guild.members()) {
            server.getProfileCache().get(player.profile().getId()).ifPresent(profiles::add);
        }
        return profiles;
    }

    @Override
    @Nullable
    public Component getTeamName(String id, MinecraftServer server) {
        Guild guild = GuildHandler.get(server, UUID.fromString(id));
        return Optionull.map(guild, Guild::getDisplayName);
    }

    @Override
    @Nullable
    public String getTeamId(ServerPlayer player) {
        Guild guild = GuildHandler.get(player);
        if (guild == null) return null;
        return guild.id().toString();
    }

    @Override
    public boolean isMember(String id, MinecraftServer server, UUID player) {
        ServerPlayer serverPlayer = server.getPlayerList().getPlayer(player);
        if (serverPlayer == null) return true;
        Guild guild = GuildHandler.get(serverPlayer);
        if (guild == null) return false;
        return guild.members().isMember(player);
    }

    @Override
    public boolean canBreakBlock(String id, MinecraftServer server, BlockPos pos, UUID player) {
        return hasPermission(MemberPermissions.BREAK_BLOCKS, id, server, player);
    }

    @Override
    public boolean canPlaceBlock(String id, MinecraftServer server, BlockPos pos, UUID player) {
        return hasPermission(MemberPermissions.PLACE_BLOCKS, id, server, player);
    }

    @Override
    public boolean canExplodeBlock(String id, MinecraftServer server, BlockPos pos, Explosion explosion, UUID player) {
        return hasPermission(MemberPermissions.EXPLODE_BLOCKS, id, server, player);
    }

    @Override
    public boolean canInteractWithBlock(String id, MinecraftServer server, BlockPos pos, InteractionType type, UUID player) {
        return hasPermission(MemberPermissions.INTERACT_WITH_BLOCKS, id, server, player);
    }

    @Override
    public boolean canInteractWithEntity(String id, MinecraftServer server, Entity entity, UUID player) {
        return hasPermission(MemberPermissions.INTERACT_WITH_ENTITIES, id, server, player);
    }

    @Override
    public boolean canDamageEntity(String id, MinecraftServer server, Entity entity, UUID player) {
        return hasPermission(MemberPermissions.DAMAGE_ENTITIES, id, server, player);
    }

    private boolean hasPermission(String perm, String id, MinecraftServer server, UUID player) {
        if (isMember(id, server, player)) return true;
        ServerPlayer serverPlayer = server.getPlayerList().getPlayer(player);
        if (serverPlayer == null) return true;
        Party party = PartyHandler.get(serverPlayer);
        if (party == null) return false;
        return party.members().get(serverPlayer.getUUID()).hasPermission(perm);
    }

    public void addPlayerToTeam(MinecraftServer server, UUID playerId, Guild guild) {
        ServerPlayer player = server.getPlayerList().getPlayer(playerId);
        if (player == null) return;

        Set<ChunkPos> removed = new HashSet<>();
        for (Team team : TeamSaveData.getTeams(server)) { // TODO throws ConcurrentModificationException rarely :sadge:
            if (team.name().equals(guild.id().toString())) {
                TeamSaveData.addTeamMember(player, team);
                return;
            }
            removed.addAll(TeamSaveData.removeTeamMember(player, team));
        }
        Team team = TeamSaveData.getOrCreateTeam(player, guild.id().toString());
        // Transfer chunks to new team if the old team was removed
        removed.forEach(chunkPos -> ClaimSaveData.set(player.getLevel(), chunkPos, new ClaimInfo(team.teamId(), ClaimType.CLAIMED)));
    }

    public void removePlayerFromTeam(MinecraftServer server, UUID playerId, Guild guild) {
        ServerPlayer player = server.getPlayerList().getPlayer(playerId);
        if (player == null) return;

        for (Team team : TeamSaveData.getTeams(server)) {
            if (team.name().equals(guild.id().toString())) {
                TeamSaveData.removeTeamMember(player, team);
                return;
            }
        }
    }

    public void disbandTeam(MinecraftServer server, UUID playerId, Guild guild) {
        ServerPlayer player = server.getPlayerList().getPlayer(playerId);
        if (player == null) return;

        for (Team team : TeamSaveData.getTeams(server)) {
            if (team.name().equals(guild.id().toString())) {
                TeamSaveData.disband(team, player.server);
                return;
            }
        }
    }
}
