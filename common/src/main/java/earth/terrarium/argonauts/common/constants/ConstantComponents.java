package earth.terrarium.argonauts.common.constants;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;

public class ConstantComponents {
    public static final Component PARTY_CHAT_TITLE = Component.translatable("gui.argonauts.party_chat.title");
    public static final Component PARTY_MEMBERS_TITLE = Component.translatable("gui.argonauts.party_members.title");
    public static final Component PARTY_SETTING_TITLE = Component.translatable("gui.argonauts.party_settings.title");
    public static final Component PARTY_CREATE = Component.translatable("text.argonauts.member.party_create");

    public static final Component GUILD_CHAT_TITLE = Component.translatable("gui.argonauts.guild_chat.title");
    public static final Component GUILD_MEMBERS_TITLE = Component.translatable("gui.argonauts.guild_members.title");

    public static final Component MEMBER_SETTINGS_TITLE = Component.translatable("gui.argonauts.member_settings.title");

    public static final Component CLICK_HERE_TO_JOIN = Component.translatable("text.argonauts.member.click_to_join");

    public static final Component MOTD = Component.translatable("text.argonauts.member.motd");
    public static final Component MOTD_HEADER = Component.translatable("text.argonauts.member.motd_header").setStyle(Style.EMPTY
        .withColor(ChatFormatting.GRAY)
        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ConstantComponents.MOTD)));

    public static final Component LINE = Component.translatable("text.argonauts.member.line").setStyle(Style.EMPTY
        .withStrikethrough(true)
        .withColor(ChatFormatting.GRAY));
    public static final Component OWNER = Component.translatable("text.argonauts.member.owner").setStyle(Style.EMPTY
        .withStrikethrough(true)
        .withColor(ChatFormatting.GRAY));
    public static final Component MEMBERS = Component.translatable("text.argonauts.member.members").setStyle(Style.EMPTY
        .withStrikethrough(true)
        .withColor(ChatFormatting.GRAY));

}
