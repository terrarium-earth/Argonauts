package earth.terrarium.argonauts.common.handlers.guild;

import net.minecraft.network.chat.Component;

public class GuildException extends RuntimeException {

    public static final GuildException YOU_ARE_NOT_IN_THIS_GUILD = new GuildException(Component.literal("You are not in this guild."));

    private final Component message;

    public GuildException(String message) {
        super(message);
        this.message = Component.literal(message);
    }

    public GuildException(Component message) {
        super(message.getString());
        this.message = message;
    }

    public Component message() {
        return message;
    }
}
