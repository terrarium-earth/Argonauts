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

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static List<Pair<UUID, Component>> getFakePlayers() {
        List<Pair<UUID, Component>> fakePlayers = new ArrayList<>();
        try {
            var ignored = FakePlayerFactory.class.getName(); // Force class loading
            Collection<FakePlayer> fakePlayerMap;
            if (fakePlayerCache == null) {
                fakePlayerCache = ((Map) UnsafeUtils.getStaticField(FakePlayerFactory.class, "fakePlayers")).values();
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
