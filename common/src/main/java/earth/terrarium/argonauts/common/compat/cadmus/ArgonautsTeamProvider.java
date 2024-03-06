package earth.terrarium.argonauts.common.compat.cadmus;

import com.mojang.authlib.GameProfile;
import earth.terrarium.argonauts.api.guild.Guild;
import earth.terrarium.argonauts.api.guild.GuildApi;
import earth.terrarium.argonauts.api.party.Party;
import earth.terrarium.argonauts.api.party.PartyApi;
import earth.terrarium.argonauts.common.handlers.base.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.guild.members.GuildMember;
import earth.terrarium.cadmus.api.claims.InteractionType;
import earth.terrarium.cadmus.api.teams.TeamProvider;
import earth.terrarium.cadmus.common.claims.ClaimHandler;
import earth.terrarium.cadmus.common.teams.TeamHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.Optionull;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class ArgonautsTeamProvider implements TeamProvider {

    @Override
    public Set<GameProfile> getTeamMembers(String id, MinecraftServer server) {
        Guild guild = GuildApi.API.get(server, UUID.fromString(id));
        if (guild == null) return new HashSet<>();
        return guild.members()
            .allMembers()
            .stream()
            .map(GuildMember::profile)
            .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    @Nullable
    public Component getTeamName(String id, MinecraftServer server) {
        var guild = GuildApi.API.get(server, UUID.fromString(id));
        return Optionull.map(guild, Guild::displayName);
    }

    @Override
    @Nullable
    public String getTeamId(MinecraftServer server, UUID player) {
        var guild = GuildApi.API.getPlayerGuild(server, player);
        return Optionull.map(guild, g -> ClaimHandler.TEAM_PREFIX + g.id().toString());
    }

    @Override
    public boolean isMember(String id, MinecraftServer server, UUID player) {
        var guild = GuildApi.API.get(server, UUID.fromString(id));
        if (guild == null) return id.equals(player.toString());
        return guild.members().isMember(player) || guild.members().isAllied(player);
    }

    @Override
    public ChatFormatting getTeamColor(String id, MinecraftServer server) {
        var guild = GuildApi.API.get(server, UUID.fromString(id));
        var result = Optionull.mapOrDefault(guild, Guild::color, ChatFormatting.AQUA);
        return result == ChatFormatting.RESET ? ChatFormatting.AQUA : result;
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
        if (TeamHelper.isMember(id, server, player)) return true;
        Guild guild = GuildApi.API.get(server, UUID.fromString(id.substring(2)));
        if (guild == null) return false;
        if (guild.settings().allowFakePlayers() && guild.members().fakePlayers().contains(player)) {
            return true;
        }

        Party party = PartyApi.API.getPlayerParty(player);
        if (party == null) return false;
        var member = party.members().get(player);
        if (member == null) return false;
        if (!member.hasPermission(perm)) return false;

        var guildMember = guild.members().get(party.members().getLeader().profile().getId());
        if (guildMember == null) return false;
        return party.members().isMember(player) && guildMember.hasPermission(MemberPermissions.TEMPORARY_GUILD_PERMISSIONS);
    }

    public void onTeamChanged(MinecraftServer server, String id, UUID player) {
        TeamProvider.super.onTeamChanged(server, id);
        server.getAllLevels().forEach(l -> ClaimHandler.clear(l, ClaimHandler.PLAYER_PREFIX + player.toString()));
    }

    @Override
    public void onTeamRemoved(MinecraftServer server, String id) {
        TeamProvider.super.onTeamRemoved(server, id);
        server.getAllLevels().forEach(l -> ClaimHandler.clear(l, ClaimHandler.TEAM_PREFIX + id));
    }

    @Override
    public boolean canModifySettings(String id, Player player) {
        try {
            Guild guild = GuildApi.API.get(player.getServer(), UUID.fromString(id));
            if (guild == null) return false;
            GuildMember member = guild.getMember(player.getUUID());
            return member != null && member.hasPermission(MemberPermissions.MANAGE_SETTINGS);
        } catch (Exception e) {
            return true;
        }
    }
}
