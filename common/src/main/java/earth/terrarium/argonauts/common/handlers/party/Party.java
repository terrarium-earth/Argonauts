package earth.terrarium.argonauts.common.handlers.party;

import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.base.members.Group;
import earth.terrarium.argonauts.common.handlers.party.members.IgnoredPartyMembers;
import earth.terrarium.argonauts.common.handlers.party.members.PartyMember;
import earth.terrarium.argonauts.common.handlers.party.members.PartyMembers;
import earth.terrarium.argonauts.common.handlers.party.settings.DefaultPartySettings;
import earth.terrarium.argonauts.common.handlers.party.settings.PartySettings;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public final class Party extends Group<PartyMember> {
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
    public PartyMember getMember(Player player) throws MemberException {
        if (!this.members().isMember(player.getUUID())) {
            throw MemberException.YOU_ARE_NOT_IN_THIS_PARTY;
        }
        return this.members().get(player.getUUID());
    }

    public PartySettings settings() {return settings;}

    public IgnoredPartyMembers ignored() {return ignored;}
}
