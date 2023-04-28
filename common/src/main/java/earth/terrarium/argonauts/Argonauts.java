package earth.terrarium.argonauts;

import earth.terrarium.argonauts.common.network.NetworkHandler;
import earth.terrarium.argonauts.common.registries.ModMenus;
import net.minecraft.SharedConstants;

public class Argonauts {
    public static final String MOD_ID = "argonauts";

    public static void init() {
        ModMenus.MENUS.init();
        NetworkHandler.init();
        SharedConstants.IS_RUNNING_IN_IDE = true; //TODO REMOVE
    }
}