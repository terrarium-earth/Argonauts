package earth.terrarium.argonauts.common.utils.fabric;

import com.mojang.datafixers.util.Pair;
import com.teamresourceful.resourcefullib.common.utils.UnsafeUtils;
import earth.terrarium.argonauts.compat.fabric.placeholderapi.ArgonautsPlaceholders;
import net.fabricmc.fabric.api.entity.FakePlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.*;

public class ModUtilsImpl {

    private static Collection<FakePlayer> fakePlayerCache;

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static List<Pair<UUID, Component>> getFakePlayers() {
        List<Pair<UUID, Component>> fakePlayers = new ArrayList<>();
        try {
            var ignored = FakePlayer.class.getName();
            Collection<FakePlayer> fakePlayerMap;
            if (fakePlayerCache == null) {
                fakePlayerCache = ((Map) UnsafeUtils.getStaticField(FakePlayer.class, "FAKE_PLAYER_MAP")).values();
            }
            fakePlayerMap = fakePlayerCache;

            for (FakePlayer p : fakePlayerMap) {
                fakePlayers.add(new Pair<>(p.getUUID(), p.getDisplayName()));
            }
        } catch (Throwable ignored) {}
        return fakePlayers;
    }

    public static Component getParsedComponent(Component component, ServerPlayer player) {
        return ArgonautsPlaceholders.getPlaceholder(component, player);
    }
}