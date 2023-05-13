package earth.terrarium.argonauts.common.compat.cadmus;

import earth.terrarium.argonauts.common.handlers.base.MemberPermissions;
import earth.terrarium.argonauts.common.handlers.guild.Guild;
import earth.terrarium.argonauts.common.handlers.guild.GuildHandler;
import earth.terrarium.argonauts.common.handlers.guild.members.GuildMembers;
import earth.terrarium.argonauts.common.handlers.party.Party;
import earth.terrarium.argonauts.common.handlers.party.PartyHandler;
import earth.terrarium.cadmus.api.claims.InteractionType;
import earth.terrarium.cadmus.api.teams.TeamProvider;
import earth.terrarium.cadmus.common.claims.ClaimHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.Optionull;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class ArgonautsTeamProvider implements TeamProvider {

    @Override
    @Nullable
    public Component getTeamName(String id, MinecraftServer server) {
        var guild = GuildHandler.get(server, UUID.fromString(id));
        if (guild == null) {
            var profile = server.getProfileCache().get(UUID.fromString(id));
            return profile.map(p -> Component.literal(p.getName())).orElse(null);
        }
        return guild.getDisplayName();
    }

    @Override
    public String getTeamId(MinecraftServer server, UUID player) {
        var profile = server.getProfileCache().get(player).orElse(null);
        if (profile == null) return player.toString();
        var guild = GuildHandler.getPlayerGuild(server, player);
        if (guild == null) return player.toString();
        return guild.id().toString();
    }

    @Override
    public boolean isMember(String id, MinecraftServer server, UUID player) {
        var profile = server.getProfileCache().get(player).orElse(null);
        if (profile == null) return false;
        var guild = GuildHandler.get(server, UUID.fromString(id));
        if (guild == null) return id.equals(player.toString());
        return guild.members().isMember(player);
    }

    @Override
    public ChatFormatting getTeamColor(String id, MinecraftServer server) {
        var guild = GuildHandler.get(server, UUID.fromString(id));
        var result = Optionull.mapOrDefault(guild, Guild::getColor, ChatFormatting.AQUA);
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
        if (isMember(id, server, player)) return true;
        Party party = PartyHandler.getPlayerParty(player);
        if (party == null) return false;
        var member = party.members().get(player);
        if (member == null) return false;
        if (member.hasPermission(perm)) return true;

        Guild guild = GuildHandler.get(server, UUID.fromString(id));
        if (guild == null) return false;
        if (guild.settings().allowFakePlayers() && guild.members() instanceof GuildMembers members && members.fakePlayers().contains(player)) {
            return true;
        }
        var guildMember = guild.members().get(player);
        if (guildMember == null) return false;
        return party.members().isMember(player) && guildMember.hasPermission(MemberPermissions.TEMPORARY_GUILD_PERMISSIONS);
    }

    public void onTeamChanged(MinecraftServer server, UUID player) {
        server.getAllLevels().forEach(l -> ClaimHandler.clear(l, player.toString()));
    }

    public void onTeamRemoved(MinecraftServer server, String id) {
        server.getAllLevels().forEach(l -> ClaimHandler.clear(l, id));
    }
}
