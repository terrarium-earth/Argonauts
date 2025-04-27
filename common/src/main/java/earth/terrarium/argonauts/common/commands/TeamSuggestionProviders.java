package earth.terrarium.argonauts.common.commands;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import earth.terrarium.argonauts.api.teams.guild.Guild;
import earth.terrarium.argonauts.api.teams.guild.GuildApi;
import earth.terrarium.argonauts.api.teams.party.Party;
import earth.terrarium.argonauts.api.teams.party.PartyApi;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class TeamSuggestionProviders {

    public static final SuggestionProvider<CommandSourceStack> PLAYERS_IN_GUILD_SUGGESTION_PROVIDER = (context, builder) -> {
        Set<UUID> players = GuildApi.API.getAllGuildsByPlayer(context.getSource().getLevel()).keySet();
        return SharedSuggestionProvider.suggest(players
            .stream()
            .map(uuid -> context.getSource().getServer().getPlayerList().getPlayer(uuid))
            .filter(Objects::nonNull)
            .map(member -> member.getGameProfile().getName()), builder);
    };

    public static final SuggestionProvider<CommandSourceStack> PLAYERS_IN_PARTY_SUGGESTION_PROVIDER = (context, builder) -> {
        Set<UUID> players = PartyApi.API.getAllPartiesByPlayer(context.getSource().getLevel()).keySet();
        return SharedSuggestionProvider.suggest(players
            .stream()
            .map(uuid -> context.getSource().getServer().getPlayerList().getPlayer(uuid))
            .filter(Objects::nonNull)
            .map(member -> member.getGameProfile().getName()), builder);
    };

    public static final SuggestionProvider<CommandSourceStack> CURRENT_GUILD_MEMBERS_SUGGESTION_PROVIDER = (context, builder) -> {
        ServerPlayer player = context.getSource().getPlayerOrException();
        Guild guild = GuildApi.API.getPlayerGuild(player).orElse(null);
        if (guild == null) throw TeamExceptions.NOT_IN_GUILD.create();
        return SharedSuggestionProvider.suggest(guild.onlineMembers(player.level())
            .stream()
            .map(member -> member.getGameProfile().getName()), builder);
    };

    public static final SuggestionProvider<CommandSourceStack> CURRENT_PARTY_MEMBERS_SUGGESTION_PROVIDER = (context, builder) -> {
        ServerPlayer player = context.getSource().getPlayerOrException();
        Party party = PartyApi.API.getPlayerParty(player).orElse(null);
        if (party == null) throw TeamExceptions.NOT_IN_PARTY.create();
        return SharedSuggestionProvider.suggest(party.onlineMembers(player.level())
            .stream()
            .map(member -> member.getGameProfile().getName()), builder);
    };
}
