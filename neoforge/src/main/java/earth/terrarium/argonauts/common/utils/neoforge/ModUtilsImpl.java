package earth.terrarium.argonauts.common.utils.neoforge;

import com.mojang.datafixers.util.Pair;
import com.teamresourceful.resourcefullib.common.lib.Constants;
import com.teamresourceful.resourcefullib.common.utils.UnsafeUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.FakePlayerFactory;

import java.util.*;

public class ModUtilsImpl {

    private static Collection<FakePlayer> fakePlayerCache;
    private static long lastTry = 0;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static List<Pair<UUID, Component>> getFakePlayers() {
        boolean lessThan5Seconds = System.currentTimeMillis() - lastTry < 5000;
        List<Pair<UUID, Component>> fakePlayers = new ArrayList<>();
        if (lessThan5Seconds && fakePlayerCache == null) {
            return fakePlayers;
        }
        try {
            Collection<FakePlayer> fakePlayerMap;
            if (fakePlayerCache == null) {
                var ignored = new FakePlayerFactory(); // Force class loading
                fakePlayerCache = ((Map) UnsafeUtils.getStaticField(FakePlayerFactory.class, "fakePlayers")).values();
                lastTry = System.currentTimeMillis();
            }
            fakePlayerMap = fakePlayerCache;

            for (FakePlayer p : fakePlayerMap) {
                fakePlayers.add(new Pair<>(p.getUUID(), p.getDisplayName()));
            }
        } catch (Exception e) {
            Constants.LOGGER.error("Failed to get fake players", e);
            e.printStackTrace();
        }
        return fakePlayers;
    }

    public static Component getParsedComponent(Component component, ServerPlayer player) {
        return component;
    }
}
