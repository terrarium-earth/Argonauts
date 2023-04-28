package earth.terrarium.argonauts.common.handlers.party;

import earth.terrarium.argonauts.common.handlers.chat.ChatHandler;
import earth.terrarium.argonauts.common.utils.ModUtils;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;

public class PartyHandler {

    private static final Map<UUID, Party> PARTIES = new HashMap<>();
    private static final Map<UUID, UUID> PLAYER_PARTIES = new HashMap<>();

    public static UUID createParty(Player player) throws PartyException {
        if (PLAYER_PARTIES.containsKey(player.getUUID())) {
            throw PartyException.ALREADY_IN_PARTY;
        }
        UUID id = ModUtils.generate(Predicate.not(PARTIES::containsKey), UUID::randomUUID);
        PARTIES.put(id, new Party(id, player));
        PLAYER_PARTIES.put(player.getUUID(), id);
        return id;
    }

    /**
     * Returns the party with the given id.
     */
    @Nullable
    public static Party get(UUID id) {
        return PARTIES.get(id);
    }

    /**
     * Returns the party the player is in.
     */
    public static Party get(Player player) {
        return get(PLAYER_PARTIES.get(player.getUUID()));
    }

    public static void join(Party party, Player player) throws PartyException {
        if (PLAYER_PARTIES.containsKey(player.getUUID())) {
            throw PartyException.ALREADY_IN_PARTY;
        } else if (party.ignored().has(player.getUUID())) {
            throw PartyException.NOT_ALLOWED_TO_JOIN_PARTY;
        } else if (party.isPublic() || party.members().isInvited(player.getUUID())) {
            party.members().add(player.getGameProfile());
            PLAYER_PARTIES.put(player.getUUID(), party.id());
        } else {
            throw PartyException.NOT_ALLOWED_TO_JOIN_PARTY;
        }
    }

    public static void remove(Party party) {
        PARTIES.remove(party.id());
        party.members().forEach(member -> {
            if (PLAYER_PARTIES.get(member.profile().getId()) == party.id()) {
                PLAYER_PARTIES.remove(member.profile().getId());
            }
        });
        ChatHandler.removeParty(party);
    }

    public static void remove(UUID id, Player player) throws PartyException {
        Party party = get(id);
        if (party == null) {
            throw PartyException.PARTY_DOES_NOT_EXIST;
        } else if (PLAYER_PARTIES.get(player.getUUID()) != id) {
            throw PartyException.PLAYER_IS_NOT_IN_PARTY;
        } else if (party.members().isLeader(player.getUUID())) {
            throw PartyException.CANT_REMOVE_LEADER;
        }
        PLAYER_PARTIES.remove(player.getUUID());
        party.members().remove(player.getUUID());
    }
}
