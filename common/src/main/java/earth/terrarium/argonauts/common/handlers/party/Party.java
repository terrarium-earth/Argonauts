package earth.terrarium.argonauts.common.handlers.party;

import earth.terrarium.argonauts.common.handlers.party.members.IgnoredPartyMembers;
import earth.terrarium.argonauts.common.handlers.party.members.PartyMember;
import earth.terrarium.argonauts.common.handlers.party.members.PartyMembers;
import earth.terrarium.argonauts.common.handlers.party.settings.DefaultPartySettings;
import earth.terrarium.argonauts.common.handlers.party.settings.PartySettings;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public record Party(
    UUID id,
    PartySettings settings,
    PartyMembers members,
    IgnoredPartyMembers ignored
) {

    public Party(UUID id, Player leader) {
        this(id, new PartySettings(), new PartyMembers(leader.getGameProfile()), new IgnoredPartyMembers());
    }

    public boolean isPublic() {
        return this.settings.has(DefaultPartySettings.PUBLIC);
    }

    public PartyMember getMember(Player player) throws PartyException {
        if (!this.members.isMember(player.getUUID())) {
            throw PartyException.YOU_ARE_NOT_IN_THIS_PARTY;
        }
        return this.members.get(player.getUUID());
    }

}
