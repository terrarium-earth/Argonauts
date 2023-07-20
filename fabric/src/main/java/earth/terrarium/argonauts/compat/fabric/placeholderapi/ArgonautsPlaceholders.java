package earth.terrarium.argonauts.compat.fabric.placeholderapi;

import earth.terrarium.argonauts.Argonauts;
import earth.terrarium.argonauts.api.guild.GuildApi;
import earth.terrarium.argonauts.api.party.PartyApi;
import eu.pb4.placeholders.api.PlaceholderContext;
import eu.pb4.placeholders.api.PlaceholderResult;
import eu.pb4.placeholders.api.Placeholders;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class ArgonautsPlaceholders {

    public static void init() {
        Placeholders.register(new ResourceLocation(Argonauts.MOD_ID, "guild"), (ctx, arg) -> {
            if (!ctx.hasPlayer()) return PlaceholderResult.invalid("No Player");

            var guild = GuildApi.API.getPlayerGuild(ctx.server(), Objects.requireNonNull(ctx.player()).getUUID());
            if (guild == null) return PlaceholderResult.invalid("No Guild");

            return PlaceholderResult.value(guild.displayName());
        });

        Placeholders.register(new ResourceLocation(Argonauts.MOD_ID, "party"), (ctx, arg) -> {
            if (!ctx.hasPlayer()) return PlaceholderResult.invalid("No Player");

            var party = PartyApi.API.getPlayerParty(Objects.requireNonNull(ctx.player()).getUUID());
            if (party == null) return PlaceholderResult.invalid("No Party");

            return PlaceholderResult.value(party.displayName());
        });
    }

    public static Component getPlaceholder(Component component, ServerPlayer player) {
        return Placeholders.parseText(component, PlaceholderContext.of(player));
    }
}
