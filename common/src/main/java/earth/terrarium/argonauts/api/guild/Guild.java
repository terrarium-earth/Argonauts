package earth.terrarium.argonauts.api.guild;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.members.Group;
import earth.terrarium.argonauts.common.handlers.guild.members.GuildMember;
import earth.terrarium.argonauts.common.handlers.guild.members.GuildMembers;
import earth.terrarium.argonauts.common.handlers.guild.settings.GuildSettings;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public final class Guild extends Group<GuildMember, GuildMembers> {
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
    public GuildMember getMember(UUID player) throws MemberException {
        if (!this.members().isMember(player)) {
            throw MemberException.YOU_ARE_NOT_IN_THIS_GUILD;
        }
        return this.members().get(player);
    }

    public GuildSettings settings() {return settings;}

    public Component motd() {
        return this.settings.motd();
    }

    @Override
    public Component displayName() {
        return this.settings.displayName();
    }

    @Override
    public ChatFormatting color() {
        return this.settings.color();
    }

    public static final ByteCodec<Guild> BYTE_CODEC = ObjectByteCodec.create(
        ByteCodec.UUID.fieldOf(Guild::id),
        GuildSettings.BYTE_CODEC.fieldOf(Guild::settings),
        GuildMembers.BYTE_CODEC.fieldOf(Guild::members),
        Guild::new
    );
}
