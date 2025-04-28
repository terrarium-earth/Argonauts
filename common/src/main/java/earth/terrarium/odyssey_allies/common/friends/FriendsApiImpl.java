package earth.terrarium.odyssey_allies.common.friends;

import com.google.common.collect.HashMultimap;
import com.mojang.authlib.GameProfile;
import earth.terrarium.odyssey_allies.api.friends.FriendsApi;
import net.minecraft.world.level.Level;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class FriendsApiImpl implements FriendsApi {

    private Set<GameProfile> convert(Level level, Set<UUID> ids) {
        if (level.getServer() == null || level.getServer().getProfileCache() == null) return Set.of();
        return ids.stream().map(uuid -> level.getServer().getProfileCache().get(uuid).orElse(null)).filter(Objects::nonNull).collect(Collectors.toSet());
    }

    @Override
    public Set<GameProfile> getFriends(Level level, GameProfile player) {
        return convert(level, FriendsSaveData.read(level).getFriends().get(player.getId()));
    }

    @Override
    public boolean areFriends(Level level, GameProfile... players) {
        if (players.length < 2) {
            return false;
        }

        FriendsSaveData friendsData = FriendsSaveData.read(level);
        HashMultimap<UUID, UUID> friendsMap = friendsData.getFriends();

        for (int i = 0; i < players.length; i++) {
            UUID playerId = players[i].getId();
            Set<UUID> playerFriends = friendsMap.get(playerId);

            for (int j = 0; j < players.length; j++) {
                if (i == j) continue;
                if (!playerFriends.contains(players[j].getId())) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void resetFriends(Level level, GameProfile player) {
        if (level.getServer() == null || level.getServer().getProfileCache() == null) return;
        FriendsSaveData friendsData = FriendsSaveData.read(level);
        Set<UUID> friends = friendsData.getFriends().get(player.getId());
        for (UUID friend : friends) {
            removeFriend(level, player, level.getServer().getProfileCache().get(friend).orElse(null));
        }
    }

    @Override
    public void sendFriendRequest(Level level, GameProfile requester, GameProfile target) {
        FriendsSaveData friendsData = FriendsSaveData.read(level);
        HashMultimap<UUID, UUID> friendRequestsMap = friendsData.getFriendRequests();
        friendRequestsMap.put(requester.getId(), target.getId());
    }

    @Override
    public Set<GameProfile> getPendingRequests(Level level, GameProfile player) {
        return convert(level, FriendsSaveData.read(level).getFriendRequests().get(player.getId()));
    }

    @Override
    public void acceptFriendRequest(Level level, GameProfile requester, GameProfile target) {
        FriendsSaveData friendsData = FriendsSaveData.read(level);
        HashMultimap<UUID, UUID> friendRequestsMap = friendsData.getFriendRequests();
        HashMultimap<UUID, UUID> friendsMap = friendsData.getFriends();

        if (friendRequestsMap.containsKey(target.getId())) {
            friendRequestsMap.remove(target.getId(), requester.getId());
            friendRequestsMap.remove(requester.getId(), target.getId());
            friendsMap.put(requester.getId(), target.getId());
            friendsMap.put(target.getId(), requester.getId());
            friendsData.setDirty();
        }
    }

    @Override
    public void denyFriendRequest(Level level, GameProfile requester, GameProfile target) {
        FriendsSaveData friendsData = FriendsSaveData.read(level);
        HashMultimap<UUID, UUID> friendRequestsMap = friendsData.getFriendRequests();
        if (friendRequestsMap.containsKey(target.getId())) {
            friendRequestsMap.remove(target.getId(), requester.getId());
            friendRequestsMap.remove(requester.getId(), target.getId());
            friendsData.setDirty();
        }
    }

    @Override
    public void removeFriend(Level level, GameProfile player, GameProfile target) {
        FriendsSaveData friendsData = FriendsSaveData.read(level);
        HashMultimap<UUID, UUID> friendsMap = friendsData.getFriends();
        if (friendsMap.containsKey(player.getId())) {
            friendsMap.remove(player.getId(), target.getId());
            friendsMap.remove(target.getId(), player.getId());
            friendsData.setDirty();
        }
    }
}
