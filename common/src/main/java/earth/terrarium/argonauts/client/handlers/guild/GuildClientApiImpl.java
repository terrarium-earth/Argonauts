package earth.terrarium.argonauts.client.handlers.guild;

import earth.terrarium.argonauts.api.client.guild.GuildClientApi;
import earth.terrarium.argonauts.api.guild.Guild;
import net.minecraft.client.player.LocalPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class GuildClientApiImpl implements GuildClientApi {
    private static final Map<UUID, Guild> GUILDS = new HashMap<>();
    private static final Map<UUID, UUID> PLAYER_GUILDS = new HashMap<>();

    @Override
    public @Nullable Guild get(UUID id) {
        return GUILDS.get(id);
    }

    @Override
    public @Nullable Guild getPlayerGuild(LocalPlayer player) {
        return getPlayerGuild(player.getUUID());
    }

    @Override
    public @Nullable Guild getPlayerGuild(UUID player) {
        return GUILDS.get(PLAYER_GUILDS.get(player));
    }

    @Override
    public Collection<Guild> getAll() {
        return GUILDS.values();
    }

    public static void update(Set<Guild> guilds, Set<UUID> removed) {
        guilds.forEach(guild -> GUILDS.put(guild.id(), guild));
        removed.forEach(GUILDS::remove);

        PLAYER_GUILDS.clear();
        GUILDS.values().forEach(team ->
            team.members().forEach(member ->
                PLAYER_GUILDS.put(member.profile().getId(), team.id())));
    }
}
