package earth.terrarium.odyssey_allies.common.compat.placeholdersapi;

import earth.terrarium.odyssey_allies.OdysseyAllies;
import earth.terrarium.odyssey_allies.api.teams.guild.GuildApi;
import earth.terrarium.odyssey_allies.api.teams.party.PartyApi;
import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.PlaceholderResult;
import eu.pb4.placeholders.api.Placeholders;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class AlliesPlaceholders {
    public static void init() {
        Placeholders.register(OdysseyAllies.id("guild"), (ctx, arg) -> {
            if (!ctx.hasPlayer()) return PlaceholderResult.invalid("No Player");

            var guild = GuildApi.API.getPlayerGuild(ctx.world(), Objects.requireNonNull(ctx.player()).getUUID()).orElse(null);
            if (guild == null) return PlaceholderResult.invalid("No Guild");

            return PlaceholderResult.value(guild.displayName());
        });

        Placeholders.register(OdysseyAllies.id("party"), (ctx, arg) -> {
            if (!ctx.hasPlayer()) return PlaceholderResult.invalid("No Player");

            var party = PartyApi.API.getPlayerParty(Objects.requireNonNull(ctx.player()).getUUID()).orElse(null);
            if (party == null) return PlaceholderResult.invalid("No Party");

            return PlaceholderResult.value(party.displayName());
        });
    }

    public static Component getPlaceholder(Component component, ServerPlayer player) {
        return Placeholders.parseText(component, PlaceholderContext.of(player));
    }
}
