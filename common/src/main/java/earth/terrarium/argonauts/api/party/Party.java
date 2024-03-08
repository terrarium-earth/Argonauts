package earth.terrarium.argonauts.api.party;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.resourcefullib.common.utils.CommonUtils;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.members.Group;
import earth.terrarium.argonauts.common.handlers.party.members.IgnoredPartyMembers;
import earth.terrarium.argonauts.common.handlers.party.members.PartyMember;
import earth.terrarium.argonauts.common.handlers.party.members.PartyMembers;
import earth.terrarium.argonauts.common.handlers.party.settings.DefaultPartySettings;
import earth.terrarium.argonauts.common.handlers.party.settings.PartySettings;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public final class Party extends Group<PartyMember, PartyMembers> {
    private final PartySettings settings;
    private final IgnoredPartyMembers ignored;

    public Party(UUID id, PartySettings settings, PartyMembers members, IgnoredPartyMembers ignored) {
        super(id, members);
        this.settings = settings;
        this.ignored = ignored;
    }

    public Party(UUID id, Player leader) {
        this(id, new PartySettings(), new PartyMembers(leader.getGameProfile()), new IgnoredPartyMembers());
    }

    @Override
    public boolean isPublic() {
        return this.settings.has(DefaultPartySettings.PUBLIC);
    }

    @Override
    public PartyMember getMember(UUID player) throws MemberException {
        if (!this.members().isMember(player)) {
            throw MemberException.YOU_ARE_NOT_IN_THIS_PARTY;
        }
        return this.members().get(player);
    }

    public PartySettings settings() {return settings;}

    public IgnoredPartyMembers ignored() {return ignored;}

    public boolean friendlyFireEnabled() {
        return this.settings.has(DefaultPartySettings.FRIENDLY_FIRE);
    }

    @Override
    public Component displayName() {
        return CommonUtils.serverTranslatable("text.argonauts.party_name", this.members().getLeader().profile().getName());
    }

    @Override
    public ChatFormatting color() {
        return ChatFormatting.WHITE;
    }

    public static final ByteCodec<Party> BYTE_CODEC = ObjectByteCodec.create(
        ByteCodec.UUID.fieldOf(Party::id),
        PartySettings.BYTE_CODEC.fieldOf(Party::settings),
        PartyMembers.BYTE_CODEC.fieldOf(Party::members),
        ByteCodec.UUID.setOf().map(IgnoredPartyMembers::new, IgnoredPartyMembers::getIgnored).fieldOf(Party::ignored),
        Party::new
    );
}
