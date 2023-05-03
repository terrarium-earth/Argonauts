package earth.terrarium.argonauts.common.handlers.guild;

import earth.terrarium.argonauts.common.handlers.guild.members.GuildMember;
import earth.terrarium.argonauts.common.handlers.guild.members.GuildMembers;
import earth.terrarium.argonauts.common.handlers.guild.settings.GuildSettings;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public record Guild(
    UUID id,
    GuildSettings settings,
    GuildMembers members
) {

    public Guild(UUID id, Player leader) {
        this(id, new GuildSettings(), new GuildMembers(leader.getGameProfile()));
    }

    public boolean isPublic() {
        return this.settings.isPublic();
    }

    public GuildMember getMember(Player player) throws GuildException {
        if (!this.members.isMember(player.getUUID())) {
            throw GuildException.YOU_ARE_NOT_IN_THIS_GUILD;
        }
        return this.members.get(player.getUUID());
    }
}
