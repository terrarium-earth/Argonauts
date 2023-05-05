package earth.terrarium.argonauts.common.handlers.base.members;

import earth.terrarium.argonauts.common.handlers.base.MemberException;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public abstract class Group<T extends Member> {
    private final UUID id;
    protected final Members<T> members;

    public Group(UUID id, Members<T> members) {
        this.id = id;
        this.members = members;
    }

    public abstract boolean isPublic();

    public abstract T getMember(Player player) throws MemberException;

    public UUID id() {return id;}

    public Members<T> members() {return members;}
}
