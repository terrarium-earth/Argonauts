package earth.terrarium.odyssey_guilds.api.friends;

import com.mojang.authlib.GameProfile;
import earth.terrarium.odyssey_guilds.api.ApiHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Set;

public interface FriendsApi {
    FriendsApi API = ApiHelper.load(FriendsApi.class);


    /**
     * Gets the friends of the player.
     *
     * @param level     the level
     * @param player    the player
     * @return the set of friends
     */
    Set<GameProfile> getFriends(Level level, GameProfile player);

    /**
     * Gets the friends of the player.
     *
     * @param player    the player
     * @return the set of friends
     */
    Set<GameProfile> getFriends(Player player);

    /**
     * Checks if the players are friends.
     *
     * @param level     the level
     * @param players   the players
     * @return true if they are friends, false otherwise
     */
    boolean areFriends(Level level, GameProfile ... players);

    /**
     * Resets the friends of the player.
     * @param level     the level
     * @param player    the player
     */
    void resetFriends(Level level, GameProfile player);

    /**
     * Resets the friends of the player.
     * @param player    the player
     */
    void resetFriends(Player player);

    /**
     * Sends a friend request to the target player.
     *
     * @param level     the level
     * @param requester the requester
     * @param target    the target
     */
    void sendFriendRequest(Level level, GameProfile requester, GameProfile target);


    /**
     * Gets the friend requests of the player.
     *
     * @param level     the level
     * @param player    the player
     * @return the set of friend requests
     */
    Set<GameProfile> getFriendRequests(Level level, GameProfile player);

    /**
     * Gets the friend requests of the player.
     *
     * @param player    the player
     * @return the set of friend requests
     */
    Set<GameProfile> getFriendRequests(Player player);

    /**
     * Accepts a friend request from the target player.
     *
     * @param level     the level
     * @param requester the requester
     * @param target    the target
     */
    void acceptFriendRequest(Level level, GameProfile requester, GameProfile target);

    /**
     * Denies a friend request from the target player.
     *
     * @param level     the level
     * @param requester the requester
     * @param target    the target
     */
    void denyFriendRequest(Level level, GameProfile requester, GameProfile target);

    /**
     * Removes a friend from the player's friends list.
     *
     * @param level     the level
     * @param player    the player
     * @param target    the target
     */
    void removeFriend(Level level, GameProfile player, GameProfile target);

    /**
     * Removes a friend from the player's friends list.
     *
     * @param player    the player
     * @param target    the target
     */
    void removeFriend(Player player, GameProfile target);
}
