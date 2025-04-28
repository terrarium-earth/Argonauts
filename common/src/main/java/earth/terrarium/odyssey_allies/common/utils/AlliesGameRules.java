package earth.terrarium.odyssey_allies.common.utils;

import com.teamresourceful.resourcefullib.common.exceptions.NotImplementedException;
import dev.architectury.injectables.annotations.ExpectPlatform;
import earth.terrarium.odyssey_allies.OdysseyAllies;
import net.minecraft.world.level.GameRules;

public class AlliesGameRules {

    public static final GameRules.Key<GameRules.IntegerValue> MAX_GUILD_MEMBERS = register(
        "maxGuildMembers",
        GameRules.Category.MISC,
        createIntRule(OdysseyAllies.DEFAULT_MAX_GUILD_MEMBERS));

    public static final GameRules.Key<GameRules.IntegerValue> MAX_PARTY_MEMBERS = register(
        "maxPartyMembers",
        GameRules.Category.MISC,
        createIntRule(OdysseyAllies.DEFAULT_MAX_PARTY_MEMBERS));

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
