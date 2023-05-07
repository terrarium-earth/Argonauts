package earth.terrarium.argonauts;

import earth.terrarium.argonauts.common.constants.ConstantComponents;
import earth.terrarium.argonauts.common.handlers.guild.Guild;
import earth.terrarium.argonauts.common.handlers.guild.GuildHandler;
import earth.terrarium.argonauts.common.network.NetworkHandler;
import earth.terrarium.argonauts.common.registries.ModMenus;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class Argonauts {
    public static final String MOD_ID = "argonauts";

    public static void init() {
        ModMenus.MENUS.init();
        NetworkHandler.init();
    }

    // Message of the day
    public static void onPlayerJoin(Player player) {
        if (!player.getLevel().isClientSide()) {
            Guild guild = GuildHandler.get((ServerPlayer) player);
            if (guild == null) return;
            Component motd = guild.settings().motd();
            if (motd.getString().isEmpty()) return;

            player.displayClientMessage(ConstantComponents.MOTD_1.copy().setStyle(Style.EMPTY
                .withColor(ChatFormatting.GRAY)
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ConstantComponents.MOTD))
            ), false);

            motd = Component.literal(motd.getString()
                .replace("&&", "§")
                .replace("\\n", "\n")
            ).setStyle(motd.getStyle()
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ConstantComponents.MOTD)));
            player.displayClientMessage(motd, false);
            player.displayClientMessage(ConstantComponents.MOTD_2.copy().setStyle(Style.EMPTY
                .withStrikethrough(true)
                .withColor(ChatFormatting.GRAY)
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ConstantComponents.MOTD))
            ), false);
        }
    }
}