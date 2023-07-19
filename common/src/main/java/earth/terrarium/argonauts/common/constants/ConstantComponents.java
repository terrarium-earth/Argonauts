package earth.terrarium.argonauts.common.constants;

import earth.terrarium.cadmus.common.util.ModUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;

public class ConstantComponents {
    public static final Component PARTY_CHAT_TITLE = Component.translatable("gui.argonauts.party_chat.title");
    public static final Component PARTY_MEMBERS_TITLE = Component.translatable("gui.argonauts.party_members.title");
    public static final Component PARTY_SETTING_TITLE = Component.translatable("gui.argonauts.party_settings.title");
    public static final Component GUILD_CHAT_TITLE = Component.translatable("gui.argonauts.guild_chat.title");
    public static final Component GUILD_MEMBERS_TITLE = Component.translatable("gui.argonauts.guild_members.title");
    public static final Component MEMBER_SETTINGS_TITLE = Component.translatable("gui.argonauts.member_settings.title");

    public static final Component PARTY_CREATE = ModUtils.serverTranslation("text.argonauts.member.party_create");


    public static final Component CLICK_HERE_TO_JOIN = ModUtils.serverTranslation("text.argonauts.member.click_to_join");

    public static final Component MOTD = ModUtils.serverTranslation("text.argonauts.member.motd");
    public static final Component MOTD_HEADER = ModUtils.serverTranslation("text.argonauts.member.motd_header").copy().setStyle(Style.EMPTY
        .withColor(ChatFormatting.GRAY)
        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ConstantComponents.MOTD)));

    public static final Component LINE = ModUtils.serverTranslation("text.argonauts.member.line").copy().setStyle(Style.EMPTY
        .withStrikethrough(true)
        .withColor(ChatFormatting.GRAY));
    public static final Component OWNER = ModUtils.serverTranslation("text.argonauts.member.owner").copy().setStyle(Style.EMPTY
        .withStrikethrough(true)
        .withColor(ChatFormatting.GRAY));
    public static final Component MEMBERS = ModUtils.serverTranslation("text.argonauts.member.members").copy().setStyle(Style.EMPTY
        .withStrikethrough(true)
        .withColor(ChatFormatting.GRAY));

    public static final Component KEY_OPEN_PARTY_CHAT = Component.translatable("key.argonauts.open_party_chat");
    public static final Component KEY_OPEN_GUILD_CHAT = Component.translatable("key.argonauts.open_guild_chat");
    public static final Component ODYSSEY_CATEGORY = Component.translatable("key.categories.project_odyssey");
}
