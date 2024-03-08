package earth.terrarium.argonauts.common.handlers.base.members;

import earth.terrarium.argonauts.common.handlers.base.MemberException;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public abstract class Group<T extends Member, U extends Members<T>> {
    private final UUID id;
    private final U members;

    public Group(UUID id, U members) {
        this.id = id;
        this.members = members;
    }

    public abstract boolean isPublic();

    public T getMember(Player player) throws MemberException {
        return this.getMember(player.getUUID());
    }

    public abstract T getMember(UUID player) throws MemberException;

    public UUID id() {
        return id;
    }

    public U members() {
        return members;
    }

    public abstract Component displayName();

    public abstract ChatFormatting color();
}
