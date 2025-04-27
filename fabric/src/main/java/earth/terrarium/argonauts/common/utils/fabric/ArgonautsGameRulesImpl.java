package earth.terrarium.argonauts.common.utils.fabric;

import net.fabricmc.fabric.api.gamerule.v1.GameRuleFactory;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleRegistry;
import net.minecraft.world.level.GameRules;

public class ArgonautsGameRulesImpl {

    public static <T extends GameRules.Value<T>> GameRules.Key<T> register(String name, GameRules.Category category, GameRules.Type<T> type) {
        return GameRuleRegistry.register(name, category, type);
    }

    public static GameRules.Type<GameRules.IntegerValue> createIntRule(int defaultValue) {
        return GameRuleFactory.createIntRule(defaultValue);
    }
}
