package earth.terrarium.argonauts.api.guild;

import earth.terrarium.argonauts.api.ApiHelper;
import earth.terrarium.argonauts.common.handlers.base.MemberException;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

public interface GuildApi {

    GuildApi API = ApiHelper.load(GuildApi.class);

    /**
     * Creates a new guild with the given player as the leader.
     *
     * @param player      The player to create the guild for.
     * @param displayName The name of the guild.
     * @throws MemberException If the player is already in a guild.
     */
    void createGuild(ServerPlayer player, Component displayName) throws MemberException;

    /**
     * Gets the guild with the given id.
     *
     * @param server The server.
     * @param id     The id of the guild.
     * @return The guild with the given id, or null if the guild does not exist.
     */
    @Nullable
    Guild get(MinecraftServer server, UUID id);

    /**
     * Gets the guild the player is in.
     *
     * @param player The player to get the guild for.
     * @return The guild the player is in, or null if the player is not in a guild.
     */
    @Nullable
    Guild get(ServerPlayer player);

    /**
     * Gets the guild the player is in.
     *
     * @param server The server.
     * @param player The player to get the guild for.
     * @return The guild the player is in, or null if the player is not in a guild.
     */
    @Nullable
    Guild getPlayerGuild(MinecraftServer server, UUID player);

    /**
     * Gets all guilds.
     *
     * @param server The server.
     * @return All guilds.
     */
    Collection<Guild> getAll(MinecraftServer server);

    /**
     * Adds the given player to the given guild if the player is invited.
     *
     * @param guild  The guild to join.
     * @param player The player to join the guild.
     * @throws MemberException If the player is not allowed to join the guild or already in a guild.
     */
    void tryJoin(Guild guild, ServerPlayer player) throws MemberException;

    /**
     * Adds the given player to the given guild.
     *
     * @param guild  The guild to join.
     * @param player The player to join the guild.
     * @throws MemberException If the player is already in a guild.
     */
    void join(Guild guild, ServerPlayer player) throws MemberException;

    /**
     * Kicks the given player from the given guild.
     *
     * @param id     The id of the guild to leave.
     * @param player The player to leave the guild.
     * @throws MemberException If the guild does not exist, the player is not in the guild or the player is the owner.
     */
    void leave(UUID id, ServerPlayer player) throws MemberException;

    /**
     * Disbands the given guild.
     *
     * @param guild  The guild to disband.
     * @param server The server.
     */
    void disband(Guild guild, MinecraftServer server);

    /**
     * Removes the given guild.
     *
     * @param force  Whether to force remove the guild.
     * @param guild  The guild to remove.
     * @param server The server.
     */
    void remove(boolean force, Guild guild, MinecraftServer server);
}
