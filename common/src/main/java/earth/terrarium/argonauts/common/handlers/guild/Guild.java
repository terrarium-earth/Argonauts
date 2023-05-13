package earth.terrarium.argonauts.common.handlers.guild;

import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.members.Group;
import earth.terrarium.argonauts.common.handlers.guild.members.GuildMember;
import earth.terrarium.argonauts.common.handlers.guild.members.GuildMembers;
import earth.terrarium.argonauts.common.handlers.guild.settings.GuildSettings;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public final class Guild extends Group<GuildMember> {
    private final GuildSettings settings;

    public Guild(UUID id, GuildSettings settings, GuildMembers members) {
        super(id, members);
        this.settings = settings;
    }

    public Guild(UUID id, Player owner) {
        this(id, new GuildSettings(), new GuildMembers(owner.getGameProfile()));
    }

    @Override
    public boolean isPublic() {
        return false;
    }

    @Override
    public GuildMember getMember(Player player) throws MemberException {
        if (!this.members().isMember(player.getUUID())) {
            throw MemberException.YOU_ARE_NOT_IN_THIS_GUILD;
        }
        return this.members().get(player.getUUID());
    }

    public GuildSettings settings() {return settings;}

    public Component getMotd() {
        return this.settings.motd();
    }

    public Component getDisplayName() {
        return this.settings.displayName();
    }

    public ChatFormatting getColor() {
        return this.settings.color();
    }
}
