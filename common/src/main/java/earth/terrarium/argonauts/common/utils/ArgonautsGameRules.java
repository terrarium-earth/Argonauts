package earth.terrarium.argonauts.common.utils;

import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.argonauts.Argonauts;
import net.minecraft.world.level.GameRules;

public class ArgonautsGameRules {

    public static final GameRules.Key<GameRules.IntegerValue> MAX_GUILD_MEMBERS = register(
        "maxGuildMembers",
        GameRules.Category.MISC,
        createIntRule(Argonauts.DEFAULT_MAX_GUILD_MEMBERS));

    public static final GameRules.Key<GameRules.IntegerValue> MAX_PARTY_MEMBERS = register(
        "maxPartyMembers",
        GameRules.Category.MISC,
        createIntRule(Argonauts.DEFAULT_MAX_PARTY_MEMBERS));

    @ExpectPlatform
    private static <T extends GameRules.Value<T>> GameRules.Key<T> register(String name, GameRules.Category category, GameRules.Type<T> type) {
        throw new NotImplementedException();
    }

    @ExpectPlatform
    private static GameRules.Type<GameRules.IntegerValue> createIntRule(int defaultValue) {
        throw new NotImplementedException();
    }


    public static void init() {} // NO-OP
}
