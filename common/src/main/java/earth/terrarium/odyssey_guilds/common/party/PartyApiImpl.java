package earth.terrarium.odyssey_guilds.common.party;

import earth.terrarium.odyssey_guilds.OdysseyGuilds;
import earth.terrarium.odyssey_guilds.api.events.OdysseyGuildsEvents;
import earth.terrarium.odyssey_guilds.api.teams.MemberStatus;
import earth.terrarium.odyssey_guilds.api.teams.party.Party;
import earth.terrarium.odyssey_guilds.api.teams.party.PartyApi;
import earth.terrarium.odyssey_guilds.api.teams.settings.Setting;
import earth.terrarium.odyssey_guilds.common.compat.prometheus.PrometheusCompat;
import earth.terrarium.odyssey_guilds.common.network.NetworkHandler;
import earth.terrarium.odyssey_guilds.common.network.packets.*;
import earth.terrarium.odyssey_guilds.common.utils.OdysseyGuildsGameRules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.*;
import java.util.stream.Collectors;

public class PartyApiImpl implements PartyApi {

    private static final Map<UUID, Party> PARTIES = new HashMap<>();
    private static final Map<UUID, Party> PARTIES_BY_PLAYER = new HashMap<>();

    @Override
    public void create(Level level, Party party) {
        PARTIES.put(party.id(), party);
        PARTIES_BY_PLAYER.put(party.getOwner(), party);
        if (level instanceof ServerLevel serverLevel) {
            NetworkHandler.sendToAllClientPlayers(new ClientboundAddPartyPacket(party), serverLevel.getServer());
        }
        OdysseyGuildsEvents.CreatePartyEvent.fire(level, party);
    }

    @Override
    public void disband(Level level, Party party) {
        PARTIES.remove(party.id());
        party.members().keySet().forEach(PARTIES_BY_PLAYER::remove);
        if (level instanceof ServerLevel serverLevel) {
            NetworkHandler.sendToAllClientPlayers(new ClientboundRemovePartyPacket(party.id()), serverLevel.getServer());
        }
        OdysseyGuildsEvents.RemovePartyEvent.fire(level, party);
    }

    @Override
    public void join(Level level, Party party, UUID playerId) {
        this.modifyMember(level, party, playerId, MemberStatus.MEMBER);
    }

    @Override
    public void leave(Level level, Party party, UUID playerId) {
        if (party.members().get(playerId).status().isMember()) {
            PARTIES_BY_PLAYER.remove(playerId);
        }
        party.members().remove(playerId);
        if (level instanceof ServerLevel serverLevel) {
            NetworkHandler.sendToAllClientPlayers(new ClientboundLeavePartyPacket(party.id(), playerId), serverLevel.getServer());
        }
        OdysseyGuildsEvents.RemovePartyMemberEvent.fire(level, party, playerId);
    }

    @Override
    public void modifyMember(Level level, Party party, UUID playerId, MemberStatus status) {
        party.getOrCreateMember(playerId).setStatus(status);
        if (status.isMember()) {
            PARTIES_BY_PLAYER.put(playerId, party);
        }
        if (level instanceof ServerLevel serverLevel) {
            NetworkHandler.sendToAllClientPlayers(new ClientboundModifyPartyMemberPacket(party.id(), playerId, status), serverLevel.getServer());
        }
        OdysseyGuildsEvents.ModifyPartyMemberEvent.fire(level, party, playerId, status);
    }

    @Override
    public void modifyPermission(Level level, Party party, UUID playerId, String permission, boolean value) {
        party.getOrCreateMember(playerId).setPermission(permission, value);
        if (party.members().get(playerId).status().isMember()) {
            PARTIES_BY_PLAYER.put(playerId, party);
        }
        if (level instanceof ServerLevel serverLevel) {
            NetworkHandler.sendToAllClientPlayers(new ClientboundModifyPartyPermissionPacket(party.id(), playerId, permission, value), serverLevel.getServer());
        }
        OdysseyGuildsEvents.ModifyPartyMemberEvent.fire(level, party, playerId, party.getOrCreateMember(playerId).status());
    }

    @Override
    public void modifySetting(Level level, Party party, Setting<?> setting, String settingId) {
        PARTIES.put(party.id(), party);
        party.settings().put(settingId, setting);
        if (level instanceof ServerLevel serverLevel) {
            NetworkHandler.sendToAllClientPlayers(new ClientboundModifyPartySettingPacket(party.id(), setting, settingId), serverLevel.getServer());
        }
        OdysseyGuildsEvents.PartyChangedEvent.fire(level, party);
    }

    @Override
    public Optional<Party> get(UUID id) {
        return Optional.ofNullable(PARTIES.get(id));
    }

    @Override
    public Optional<Party> getPlayerParty(UUID playerId) {
        return Optional.ofNullable(PARTIES_BY_PLAYER.get(playerId));
    }

    @Override
    public Optional<Party> getPlayerParty(Player player) {
        return this.getPlayerParty(player.getUUID());
    }

    @Override
    public Set<Party> getAll() {
        return PARTIES.values().stream().collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Map<UUID, Party> getAllPartiesByPlayer(Level level) {
        return PARTIES_BY_PLAYER;
    }

    @Override
    public int getMaxPartyMembers(Level level, UUID ownerID) {
        int max = level.getGameRules().getInt(OdysseyGuildsGameRules.MAX_PARTY_MEMBERS);
        if (OdysseyGuilds.IS_ROLES_LOADED) {
            max = Math.min(max, PrometheusCompat.getMaxPartyMembers(level, ownerID));
        }
        return max;
    }
}
