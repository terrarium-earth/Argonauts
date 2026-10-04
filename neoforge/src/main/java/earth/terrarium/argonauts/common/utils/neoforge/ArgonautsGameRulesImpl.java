package earth.terrarium.argonauts.common.utils.neoforge;

import net.minecraft.world.level.GameRules;

public class ArgonautsGameRulesImpl {

    public static <T extends GameRules.Value<T>> GameRules.Key<T> register(String name, GameRules.Category category, GameRules.Type<T> type) {
        return GameRules.register(name, category, type);
    }

    public static GameRules.Type<GameRules.IntegerValue> createIntRule(int defaultValue) {
        return GameRules.IntegerValue.create(defaultValue);
    }
}
