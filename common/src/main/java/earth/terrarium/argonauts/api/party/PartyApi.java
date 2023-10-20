package earth.terrarium.argonauts.api.party;

import earth.terrarium.argonauts.api.ApiHelper;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

public interface PartyApi {

    PartyApi API = ApiHelper.load(PartyApi.class);

    /**
     * @deprecated Use {@link #createParty(ServerPlayer)} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval(inVersion = "1.20.2")
    default void createParty(Player player) throws MemberException {
        createParty((ServerPlayer) player);
    }

    /**
     * Creates a new party with the given player as the leader.
     *
     * @param player The player to create the party for.
     * @throws MemberException If the player is already in a party.
     */
    void createParty(ServerPlayer player) throws MemberException;

    /**
     * Gets the party with the given id.
     *
     * @param id The id of the party.
     * @return The party with the given id, or null if the party does not exist.
     */
    @Nullable
    Party get(UUID id);

    /**
     * Gets the party the player is in.
     *
     * @param player The player to get the party for.
     * @return The party the player is in, or null if the player is not in a party.
     */
    @Nullable
    Party get(Player player);

    /**
     * Gets the party the player is in.
     *
     * @param player The player to get the party for.
     * @return The party the player is in, or null if the player is not in a party.
     */
    @Nullable
    Party getPlayerParty(UUID player);

    /**
     * Adds the given player to the given party.
     *
     * @param party  The party to join.
     * @param player The player to join the party.
     * @throws MemberException If the player is not allowed to join the party or already in a party.
     */
    void join(Party party, ServerPlayer player) throws MemberException;

    /**
     * Kicks the given player from the given party.
     *
     * @param id     The id of the party to leave.
     * @param player The player to leave the party.
     * @throws MemberException If the party does not exist, the player is not in the party or the player is the leader.
     */
    void leave(UUID id, ServerPlayer player) throws MemberException;

    /**
     * Disbands the given party.
     *
     * @param party  The party to disband.
     * @param server The server.
     */
    void disband(Party party, MinecraftServer server);

    /**
     * Gets all parties.
     *
     * @return All parties.
     */
    Collection<Party> getAll();
}
