package earth.terrarium.argonauts.common.constants;

import com.teamresourceful.resourcefullib.common.utils.CommonUtils;
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

    public static final Component CADMUS_PERMISSIONS = Component.translatable("argonauts.member.cadmus_permissions");
    public static final Component SETTINGS = Component.translatable("argonauts.member.settings");
    public static final Component ACTIONS = Component.translatable("argonauts.member.actions");
    public static final Component LEAVE_PARTY = Component.translatable("argonauts.member.leave_party");
    public static final Component LEAVE = Component.translatable("argonauts.member.leave");

    public static final Component PARTY_CREATE = CommonUtils.serverTranslatable("text.argonauts.member.party_create");

    public static final Component CLICK_HERE_TO_JOIN = CommonUtils.serverTranslatable("text.argonauts.member.click_to_join");

    public static final Component MOTD = CommonUtils.serverTranslatable("text.argonauts.member.motd");
    public static final Component MOTD_HEADER = CommonUtils.serverTranslatable("text.argonauts.member.motd_header").copy().setStyle(Style.EMPTY
        .withColor(ChatFormatting.GRAY)
        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ConstantComponents.MOTD)));

    public static final Component LINE = CommonUtils.serverTranslatable("text.argonauts.member.line").copy().setStyle(Style.EMPTY
        .withStrikethrough(true)
        .withColor(ChatFormatting.GRAY));
    public static final Component OWNER = CommonUtils.serverTranslatable("text.argonauts.member.owner").copy().setStyle(Style.EMPTY
        .withStrikethrough(true)
        .withColor(ChatFormatting.GRAY));
    public static final Component MEMBERS = CommonUtils.serverTranslatable("text.argonauts.member.members").copy().setStyle(Style.EMPTY
        .withStrikethrough(true)
        .withColor(ChatFormatting.GRAY));

    public static final Component KEY_OPEN_PARTY_CHAT = Component.translatable("key.argonauts.open_party_chat");
    public static final Component KEY_OPEN_GUILD_CHAT = Component.translatable("key.argonauts.open_guild_chat");
    public static final Component ODYSSEY_CATEGORY = Component.translatable("key.categories.project_odyssey");
}
