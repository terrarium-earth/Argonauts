package earth.terrarium.argonauts.common.utils.fabric;

import com.mojang.datafixers.util.Pair;
import earth.terrarium.argonauts.common.utils.UnsafeUtils;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;

import java.util.*;

public class ModUtilsImpl {
    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static List<Pair<UUID, Component>> getFakePlayers() {
        List<Pair<UUID, Component>> fakePlayers = new ArrayList<>();
        try {
            for (FakePlayer fakePlayerMap : ((Collection<FakePlayer>) ((Map) UnsafeUtils.getStaticField(FakePlayer.class, "FAKE_PLAYER_MAP")).values())) {
                fakePlayers.add(new Pair<>(fakePlayerMap.getUUID(), fakePlayerMap.getDisplayName()));
            }
        } catch (Throwable ignored) {}
        return fakePlayers;
    }
}
