package earth.terrarium.odyssey_guilds.common.guild;

import earth.terrarium.odyssey_guilds.OdysseyGuilds;
import earth.terrarium.odyssey_guilds.api.events.OdysseyGuildsEvents;
import earth.terrarium.odyssey_guilds.api.teams.MemberStatus;
import earth.terrarium.odyssey_guilds.api.teams.guild.Guild;
import earth.terrarium.odyssey_guilds.api.teams.guild.GuildApi;
import earth.terrarium.odyssey_guilds.api.teams.settings.Setting;
import earth.terrarium.odyssey_guilds.common.compat.roles.RolesCompat;
import earth.terrarium.odyssey_guilds.common.network.NetworkHandler;
import earth.terrarium.odyssey_guilds.common.network.packets.*;
import earth.terrarium.odyssey_guilds.common.utils.OdysseyGuildsGameRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class GuildApiImpl implements GuildApi {

    @Override
    public void create(Level level, Guild guild) {
        var data = GuildSaveData.read(level);
        data.guilds().put(guild.id(), guild);
        data.guildsByPlayer().put(guild.getOwner(), guild);
        if (level instanceof ServerLevel serverLevel) {
            data.setDirty();
            NetworkHandler.sendToAllClientPlayers(new ClientboundAddGuildPacket(guild), serverLevel.getServer());
        }
        OdysseyGuildsEvents.CreateGuildEvent.fire(level, guild);
    }

    @Override
    public void disband(Level level, Guild guild) {
        var data = GuildSaveData.read(level);
        data.guilds().remove(guild.id());
        guild.members().keySet().forEach(data.guildsByPlayer()::remove);
        if (level instanceof ServerLevel serverLevel) {
            data.setDirty();
            NetworkHandler.sendToAllClientPlayers(new ClientboundRemoveGuildPacket(guild.id()), serverLevel.getServer());
        }
        OdysseyGuildsEvents.RemoveGuildEvent.fire(level, guild);
    }

    @Override
    public void join(Level level, Guild guild, UUID playerId) {
        this.modifyMember(level, guild, playerId, MemberStatus.MEMBER);
    }

    @Override
    public void leave(Level level, Guild guild, UUID playerId) {
        var data = GuildSaveData.read(level);
        if (guild.members().get(playerId).status().isMember()) {
            data.guildsByPlayer().remove(playerId);
        }
        guild.members().remove(playerId);
        if (level instanceof ServerLevel serverLevel) {
            data.setDirty();
            NetworkHandler.sendToAllClientPlayers(new ClientboundLeaveGuildPacket(guild.id(), playerId), serverLevel.getServer());
        }
        OdysseyGuildsEvents.RemoveGuildMemberEvent.fire(level, guild, playerId);
    }

    @Override
    public void modifyMember(Level level, Guild guild, UUID playerId, MemberStatus status) {
        var data = GuildSaveData.read(level);
        guild.getOrCreateMember(playerId).setStatus(status);
        if (status.isMember()) {
            data.guildsByPlayer().put(playerId, guild);
        }
        if (level instanceof
            ServerLevel serverLevel) {
            data.setDirty();
            NetworkHandler.sendToAllClientPlayers(new ClientboundModifyGuildMemberPacket(guild.id(), playerId, status), serverLevel.getServer());
        }
        OdysseyGuildsEvents.ModifyGuildMemberEvent.fire(level, guild, playerId, status);
    }

    @Override
    public void modifyPermission(Level level, Guild guild, UUID playerId, String permission, boolean value) {
        var data = GuildSaveData.read(level);
        guild.getOrCreateMember(playerId).setPermission(permission, value);
        if (guild.members().get(playerId).status().isMember()) {
            data.guildsByPlayer().put(playerId, guild);
        }
        if (level instanceof ServerLevel serverLevel) {
            data.setDirty();
            NetworkHandler.sendToAllClientPlayers(new ClientboundModifyGuildPermissionPacket(guild.id(), playerId, permission, value), serverLevel.getServer());
        }
        OdysseyGuildsEvents.ModifyGuildMemberEvent.fire(level, guild, playerId, guild.getOrCreateMember(playerId).status());
    }

    @Override
    public void modifySetting(Level level, Guild guild, Setting<?> setting, String settingId) {
        var data = GuildSaveData.read(level);
        guild.settings().put(settingId, setting);
        if (level instanceof ServerLevel serverLevel) {
            data.setDirty();
            NetworkHandler.sendToAllClientPlayers(new ClientboundModifyGuildSettingPacket(guild.id(), setting, settingId), serverLevel.getServer());
        }
        OdysseyGuildsEvents.GuildChangedEvent.fire(level, guild);
    }

    @Override
    public Optional<Guild> get(Level level, UUID id) {
        return Optional.ofNullable(GuildSaveData.read(level).guilds().get(id));
    }

    @Override
    public Optional<Guild> getPlayerGuild(Level level, UUID playerId) {
        return Optional.ofNullable(GuildSaveData.read(level).guildsByPlayer().get(playerId));
    }

    @Override
    public Set<Guild> getAll(Level level) {
        return GuildSaveData.read(level).guilds().values().stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Map<UUID, Guild> getAllGuildsByPlayer(Level level) {
        return GuildSaveData.read(level).guildsByPlayer();
    }

    @Override
    public int getMaxGuildMembers(Level level, UUID ownerID) {
        int max = level.getGameRules().getInt(OdysseyGuildsGameRules.MAX_GUILD_MEMBERS);
        if (OdysseyGuilds.IS_ROLES_LOADED) {
            max = Math.min(max, RolesCompat.getMaxGuildMembers(level, ownerID));
        }
        return max;
    }
}
