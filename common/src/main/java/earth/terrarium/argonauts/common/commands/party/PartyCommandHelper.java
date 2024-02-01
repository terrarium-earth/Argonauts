package earth.terrarium.argonauts.common.commands.party;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import earth.terrarium.argonauts.api.party.Party;
import earth.terrarium.argonauts.api.party.PartyApi;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import net.minecraft.world.entity.player.Player;

public final class PartyCommandHelper {

    public static Party getPartyOrThrow(Player player, boolean otherPlayer) throws CommandSyntaxException {
        Party party = PartyApi.API.get(player);
        if (party == null) throw new SimpleCommandExceptionType(otherPlayer ?
            MemberException.PLAYER_IS_NOT_IN_PARTY.message() :
            MemberException.YOU_ARE_NOT_IN_PARTY.message()).create();
        return party;
    }
}
