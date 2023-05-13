package earth.terrarium.argonauts.common.utils.forge;

import com.mojang.datafixers.util.Pair;
import earth.terrarium.argonauts.common.utils.UnsafeUtils;
import net.minecraft.network.chat.Component;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.fml.ModList;

import java.util.*;

public class ModUtilsImpl {
    public static boolean isModLoaded(String modId) {
        return ModList.get().isLoaded(modId);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static List<Pair<UUID, Component>> getFakePlayers() {
        List<Pair<UUID, Component>> fakePlayers = new ArrayList<>();
        try {
            for (FakePlayer fakePlayerMap : ((Collection<FakePlayer>) ((Map) UnsafeUtils.getStaticField(FakePlayerFactory.class, "fakePlayers")).values())) {
                fakePlayers.add(new Pair<>(fakePlayerMap.getUUID(), fakePlayerMap.getDisplayName()));
            }
        } catch (Throwable ignored) {}
        return fakePlayers;
    }
}
