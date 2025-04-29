package earth.terrarium.odyssey_allies.common.constants;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.teamresourceful.resourcefullib.common.utils.CommonUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;

public class ConstantComponents {

    public static final Component CLICK_TO_ACCEPT = CommonUtils.serverTranslatable("command.odyssey_allies.click_to_accept");

    public static final Component MOTD = CommonUtils.serverTranslatable("motd.odyssey_allies.title");
    public static final Component MOTD_HEADER = CommonUtils.serverTranslatable("motd.odyssey_allies.header").copy().setStyle(Style.EMPTY
        .withColor(ChatFormatting.GRAY)
        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, MOTD)));
    public static final Component MOTD_LINE = CommonUtils.serverTranslatable("motd.odyssey_allies.line").copy().setStyle(Style.EMPTY
        .withStrikethrough(true)
        .withColor(ChatFormatting.GRAY));

    public static final Component ODYSSEY_CATEGORY = Component.translatable("key.categories.project_odyssey");
    public static final Component KEY_OPEN_PARTY_CHAT = Component.translatable("key.odyssey_allies.open_party_chat");
    public static final Component KEY_OPEN_GUILD_CHAT = Component.translatable("key.odyssey_allies.open_guild_chat");

    public static final Component PARTY_CHAT_TITLE = Component.translatable("gui.odyssey_allies.party_chat.title");
    public static final Component PARTY_MEMBERS_TITLE = Component.translatable("gui.odyssey_allies.party_members.title");
    public static final Component PARTY_SETTINGS_TITLE = Component.translatable("gui.odyssey_allies.party_settings.title");

    public static final Component GUILD_CHAT_TITLE = Component.translatable("gui.odyssey_allies.guild_chat.title");
    public static final Component GUILD_MEMBERS_TITLE = Component.translatable("gui.odyssey_allies.guild_members.title");
    public static final Component GUILD_SETTINGS_TITLE = Component.translatable("gui.odyssey_allies.guild_settings.title");

    public static final Component MEMBER_STATUS = Component.translatable("gui.odyssey_allies.member_status");
    public static final Component MEMBER_PERMISSIONS = Component.translatable("gui.odyssey_allies.member_permissions");
    public static final Component MEMBER_ACTIONS = Component.translatable("gui.odyssey_allies.member_actions");
    public static final Component SETTINGS = Component.translatable("gui.odyssey_allies.settings");

    public static final Component REMOVE_MEMBER = Component.translatable("gui.odyssey_allies.remove_member");
    public static final Component REMOVE = Component.translatable("gui.odyssey_allies.remove");

    public static final Component MAX_GUILD_MEMBERS = Component.translatable("gui.odyssey_allies.max_guild_members");
    public static final Component MAX_PARTY_MEMBERS = Component.translatable("gui.odyssey_allies.max_party_members");

    public static final Component FRIEND_LIST = CommonUtils.serverTranslatable("command.odyssey_allies.list_friends");
}
