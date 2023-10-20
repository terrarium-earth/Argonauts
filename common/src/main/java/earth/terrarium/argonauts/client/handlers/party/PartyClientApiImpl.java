package earth.terrarium.argonauts.client.handlers.party;

import earth.terrarium.argonauts.api.client.party.PartyClientApi;
import earth.terrarium.argonauts.api.party.Party;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PartyClientApiImpl implements PartyClientApi {
    private static final Map<UUID, Party> PARTIES = new HashMap<>();
    private static final Map<UUID, UUID> PLAYER_PARTIES = new HashMap<>();

    @Override
    public @Nullable Party get(UUID id) {
        return PARTIES.get(id);
    }

    @Override
    public @Nullable Party getPlayerParty(LocalPlayer player) {
        return getPlayerParty(player.getUUID());
    }

    @Override
    public @Nullable Party getPlayerParty(UUID player) {
        return PARTIES.get(PLAYER_PARTIES.get(player));
    }

    @Override
    public Collection<Party> getAll() {
        return PARTIES.values();
    }

    public static void update(Set<Party> parties, Set<UUID> removed) {
        parties.forEach(guild -> PARTIES.put(guild.id(), guild));
        removed.forEach(PARTIES::remove);

        PLAYER_PARTIES.clear();
        PARTIES.values().forEach(team ->
            team.members().forEach(member ->
                PLAYER_PARTIES.put(member.profile().getId(), team.id())));
    }
}
