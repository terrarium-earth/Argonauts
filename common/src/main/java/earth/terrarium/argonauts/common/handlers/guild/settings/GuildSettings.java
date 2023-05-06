package earth.terrarium.argonauts.common.handlers.guild.settings;

import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class GuildSettings {

    private GlobalPos hq = null;
    private Component displayName = CommonComponents.EMPTY;
    private Component motd = CommonComponents.EMPTY;

    public void setHq(GlobalPos hq) {
        this.hq = hq;
    }

    public GlobalPos hq() {
        return hq;
    }

    public void setDisplayName(Component displayName) {
        this.displayName = displayName;
    }

    public Component displayName() {
        return displayName;
    }

    public void setMotd(Component motd) {
        this.motd = motd;
    }

    public Component motd() {
        return motd;
    }
}
