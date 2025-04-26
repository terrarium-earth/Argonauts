package earth.terrarium.odyssey_guilds.api.teams.guild;

import earth.terrarium.odyssey_guilds.api.ApiHelper;
import earth.terrarium.odyssey_guilds.api.teams.MemberStatus;
import earth.terrarium.odyssey_guilds.api.teams.settings.Setting;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface GuildApi {

    GuildApi API = ApiHelper.load(GuildApi.class);

    /**
     * Creates a new guild.
     *
     * @param level the level
     * @param guild the guild
     */
    void create(Level level, Guild guild);

    /**
     * Removes the guild.
     *
     * @param level the level
     * @param guild the guild
     */
    void disband(Level level, Guild guild);

    /**
     * Adds the player to the guild.
     *
     * @param level    the level
     * @param guild    the guild
     * @param playerId the player ID
     */
    void join(Level level, Guild guild, UUID playerId);

    /**
     * Removes the player from the guild.
     *
     * @param level    the level
     * @param guild    the guild
     * @param playerId the player ID
     */
    void leave(Level level, Guild guild, UUID playerId);

    /**
     * Modifies the member status of the player.
     *
     * @param level    the level
     * @param guild    the guild
     * @param playerId the player ID
     * @param status   the status
     */
    void modifyMember(Level level, Guild guild, UUID playerId, MemberStatus status);

    /**
     * Modifies the permission of the player.
     *
     * @param level      the level
     * @param guild      the guild
     * @param playerId   the player ID
     * @param permission the permission
     * @param value      the value
     */
    void modifyPermission(Level level, Guild guild, UUID playerId, String permission, boolean value);

    /**
     * Modifies the setting of the guild.
     *
     * @param level     the level
     * @param guild     the guild
     * @param setting   the setting
     * @param settingId the setting ID
     */
    void modifySetting(Level level, Guild guild, Setting<?> setting, String settingId);

    /**
     * Gets a guild by its ID.
     *
     * @param level the level
     * @param id    the ID
     * @return the guild, or empty if it does not exist
     */
    Optional<Guild> get(Level level, UUID id);

    /**
     * Gets the guild of the player.
     *
     * @param level    the level
     * @param playerId the player ID
     * @return the guild, or empty if the player is not in a guild
     */
    Optional<Guild> getPlayerGuild(Level level, UUID playerId);

    /**
     * Gets the guild of the player.
     *
     * @param player the player
     * @return the guild, or empty if the player is not in a guild
     */
    default Optional<Guild> getPlayerGuild(Player player) {
        return getPlayerGuild(player.level(), player.getUUID());
    }

    /**
     * Gets all guilds.
     *
     * @param level the level
     * @return the guilds
     */
    Set<Guild> getAll(Level level);


    /**
     * Gets all guilds by player.
     *
     * @param level the level
     * @return the guilds
     */
    Map<UUID, Guild> getAllGuildsByPlayer(Level level);

    /**
     * Gets the maximum number members per guild.
     *
     * @param level   the level
     * @param ownerID the owner ID
     * @return the maximum number of members
     */
    int getMaxGuildMembers(Level level, UUID ownerID);
}
