package earth.terrarium.argonauts.common.commands.party;

import earth.terrarium.argonauts.api.party.PartyApi;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import earth.terrarium.argonauts.common.handlers.party.Party;
import net.minecraft.commands.CommandRuntimeException;
import net.minecraft.world.entity.player.Player;

public final class PartyCommandHelper {

    public static Party getPartyOrThrow(Player player, boolean otherPlayer) throws CommandRuntimeException {
        Party party = PartyApi.API.get(player);
        if (party == null) {
            throw new CommandRuntimeException(otherPlayer ? MemberException.PLAYER_IS_NOT_IN_PARTY.message() : MemberException.YOU_ARE_NOT_IN_PARTY.message());
        }
        return party;
    }
}
