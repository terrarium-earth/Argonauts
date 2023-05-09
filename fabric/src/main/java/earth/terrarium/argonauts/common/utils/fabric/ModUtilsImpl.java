package earth.terrarium.argonauts.common.utils.fabric;

import net.fabricmc.loader.api.FabricLoader;

public class ModUtilsImpl {
    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
