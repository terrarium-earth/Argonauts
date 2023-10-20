package earth.terrarium.argonauts.api.client.guild;

import earth.terrarium.argonauts.api.ApiHelper;
import earth.terrarium.argonauts.api.guild.Guild;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.UUID;

public interface GuildClientApi {

    GuildClientApi API = ApiHelper.load(GuildClientApi.class);

    /**
     * Gets the guild with the given id.
     *
     * @param id The id of the guild.
     * @return The guild with the given id, or null if the guild does not exist.
     */
    @Nullable
    Guild get(UUID id);

    /**
     * Gets the guild the player is in.
     *
     * @param player The player to get the guild for.
     * @return The guild the player is in, or null if the player is not in a guild.
     */
    @Nullable
    Guild getPlayerGuild(LocalPlayer player);

    /**
     * Gets the guild the player is in.
     *
     * @param player The player to get the guild for.
     * @return The guild the player is in, or null if the player is not in a guild.
     */
    @Nullable
    Guild getPlayerGuild(UUID player);

    /**
     * Gets all guilds.
     *
     * @return All guilds.
     */
    Collection<Guild> getAll();
}
