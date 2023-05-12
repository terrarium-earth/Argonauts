package earth.terrarium.argonauts.common.handlers.guild.settings;

import net.minecraft.ChatFormatting;
import net.minecraft.core.GlobalPos;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.Optional;

public class GuildSettings {

    private GlobalPos hq;
    private Component displayName = CommonComponents.EMPTY;
    private Component motd = CommonComponents.EMPTY;
    private ChatFormatting color = ChatFormatting.AQUA;

    public void setHq(GlobalPos hq) {
        this.hq = hq;
    }

    public Optional<GlobalPos> hq() {
        return Optional.ofNullable(hq);
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

    public void setColor(ChatFormatting color) {
        this.color = color;
    }

    public ChatFormatting color() {
        return color;
    }
}
