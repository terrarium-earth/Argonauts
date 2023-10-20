package earth.terrarium.argonauts.api.client.party;

import earth.terrarium.argonauts.api.ApiHelper;
import earth.terrarium.argonauts.api.party.Party;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

public interface PartyClientApi {

    PartyClientApi API = ApiHelper.load(PartyClientApi.class);

    /**
     * Gets the party with the given id.
     *
     * @param id The id of the party.
     * @return The party with the given id.
     */
    @Nullable
    Party get(UUID id);

    /**
     * Gets the party the player is in.
     *
     * @param player The player to get the party for.
     * @return The party the player is in.
     */
    @Nullable
    Party getPlayerParty(LocalPlayer player);

    /**
     * Gets the party the player is in.
     *
     * @param player The player to get the party for.
     * @return The party the player is in.
     */
    @Nullable
    Party getPlayerParty(UUID player);

    /**
     * Gets all parties.
     *
     * @return All parties.
     */
    Collection<Party> getAll();
}
