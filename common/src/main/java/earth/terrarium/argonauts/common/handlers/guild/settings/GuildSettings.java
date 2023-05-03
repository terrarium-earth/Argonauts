package earth.terrarium.argonauts.common.handlers.guild.settings;

import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class GuildSettings {

    private GlobalPos hq = null;
    private Component name = CommonComponents.EMPTY;
    private boolean isPublic = false;

    public void setHq(GlobalPos hq) {
        this.hq = hq;
    }

    public GlobalPos hq() {
        return hq;
    }

    public void setName(Component name) {
        this.name = name;
    }

    public Component name() {
        return name;
    }

    public void setIsPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public boolean isPublic() {
        return isPublic;
    }

}
