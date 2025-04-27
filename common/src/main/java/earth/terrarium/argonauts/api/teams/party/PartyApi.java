package earth.terrarium.argonauts.api.teams.party;

import earth.terrarium.argonauts.api.ApiHelper;
import earth.terrarium.argonauts.api.teams.MemberStatus;
import earth.terrarium.argonauts.api.teams.settings.Setting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface PartyApi {

    PartyApi API = ApiHelper.load(PartyApi.class);

    /**
     * Creates a new party.
     *
     * @param level the level
     * @param party the party
     */
    void create(Level level, Party party);

    /**
     * Removes the party.
     *
     * @param level the level
     * @param party the party
     */
    void disband(Level level, Party party);

    /**
     * Adds the player to the party.
     *
     * @param level    the level
     * @param party    the party
     * @param playerId the player ID
     */
    void join(Level level, Party party, UUID playerId);

    /**
     * Removes the player from the party.
     *
     * @param level    the level
     * @param party    the party
     * @param playerId the player ID
     */
    void leave(Level level, Party party, UUID playerId);

    /**
     * Modifies the member status of the player.
     *
     * @param level    the level
     * @param party    the party
     * @param playerId the player ID
     * @param status   the status
     */
    void modifyMember(Level level, Party party, UUID playerId, MemberStatus status);

    /**
     * Modifies the permission of the player.
     *
     * @param level      the level
     * @param party      the party
     * @param playerId   the player ID
     * @param permission the permission
     * @param value      the value
     */
    void modifyPermission(Level level, Party party, UUID playerId, String permission, boolean value);

    /**
     * Modifies the setting of the party.
     *
     * @param level     the level
     * @param party     the party
     * @param setting   the setting
     * @param settingId the setting ID
     */
    void modifySetting(Level level, Party party, Setting<?> setting, String settingId);

    /**
     * Gets a party by its ID.
     *
     * @param id the ID
     * @return the party, or empty if it does not exist
     */
    Optional<Party> get(UUID id);

    /**
     * Gets the party of the player.
     *
     * @param playerId the player ID
     * @return the party, or empty if the player is not in a party
     */
    Optional<Party> getPlayerParty(UUID playerId);

    /**
     * Gets the party of the player.
     *
     * @param player the player
     * @return the party, or empty if the player is not in a party
     */
    Optional<Party> getPlayerParty(Player player);

    /**
     * Gets all parties.
     *
     * @return the parties
     */
    Set<Party> getAll();

    /**
     * Gets all parties by player.
     *
     * @param level the level
     * @return the parties
     */
    Map<UUID, Party> getAllPartiesByPlayer(Level level);

    /**
     * Gets the maximum number of party members.
     *
     * @param level   the level
     * @param ownerID the owner ID
     * @return the maximum number of party members
     */
    int getMaxPartyMembers(Level level, UUID ownerID);
}
