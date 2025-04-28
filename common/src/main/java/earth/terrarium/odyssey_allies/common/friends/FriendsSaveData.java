package earth.terrarium.odyssey_allies.common.friends;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.authlib.GameProfile;
import com.teamresourceful.resourcefullib.common.utils.SaveHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Set;
import java.util.UUID;

public class FriendsSaveData extends SaveHandler {
    private static final FriendsSaveData CLIENT_SIDE = new FriendsSaveData();

    private final HashMultimap<UUID, UUID> friends = HashMultimap.create();
    private final HashMultimap<UUID, UUID> friendRequests = HashMultimap.create();


    protected static FriendsSaveData read(Level level) {
        return read(level, HandlerType.create(CLIENT_SIDE, FriendsSaveData::new), "odyssey_allies_friends");
    }

    protected HashMultimap<UUID, UUID> getFriends() {
        return this.friends;
    }

    protected HashMultimap<UUID, UUID> getFriendRequests() {
        return this.friendRequests;
    }

    @Override
    public void loadData(CompoundTag tag) {
        CompoundTag friendsTag = tag.getCompound("friends");
        friendsTag.getAllKeys().forEach(playerId -> {
            UUID player = UUID.fromString(playerId);
            ListTag friendsList = friendsTag.getList(playerId, Tag.TAG_STRING);
            for (Tag stringTag : friendsList) {
                UUID friend = UUID.fromString(stringTag.getAsString());
                this.friends.put(player, friend);
            }
        });
    }

    @Override
    public void saveData(CompoundTag tag) {
        CompoundTag friendsTag = new CompoundTag();
        this.friends.asMap().forEach((playerId, friends) -> {
            ListTag friendsList = new ListTag();
            for (UUID friend : friends) {
                friendsList.add(StringTag.valueOf(friend.toString()));
            }
            friendsTag.put(playerId.toString(), friendsList);
        });
        tag.put("friends", friendsTag);
    }
}
