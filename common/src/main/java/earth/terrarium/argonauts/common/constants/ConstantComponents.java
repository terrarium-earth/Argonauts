package earth.terrarium.argonauts.common.constants;

import com.teamresourceful.resourcefullib.common.utils.CommonUtils;
import earth.terrarium.argonauts.Argonauts;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;

public class ConstantComponents {

    public static final Component CLICK_TO_ACCEPT = CommonUtils.serverTranslatable("command."+ Argonauts.MOD_ID +".click_to_accept");

    public static final Component MOTD = CommonUtils.serverTranslatable("motd."+ Argonauts.MOD_ID +".title");
    public static final Component MOTD_HEADER = CommonUtils.serverTranslatable("motd."+ Argonauts.MOD_ID +".header").copy().setStyle(Style.EMPTY
        .withColor(ChatFormatting.GRAY)
        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, MOTD)));
    public static final Component MOTD_LINE = CommonUtils.serverTranslatable("motd."+ Argonauts.MOD_ID +".line").copy().setStyle(Style.EMPTY
        .withStrikethrough(true)
        .withColor(ChatFormatting.GRAY));

    public static final Component ODYSSEY_CATEGORY = Component.translatable("key.categories.project_odyssey");
    public static final Component KEY_OPEN_PARTY_CHAT = Component.translatable("key."+ Argonauts.MOD_ID +".open_party_chat");
    public static final Component KEY_OPEN_GUILD_CHAT = Component.translatable("key."+ Argonauts.MOD_ID +".open_guild_chat");

    public static final Component PARTY_CHAT_TITLE = Component.translatable("gui."+ Argonauts.MOD_ID +".party_chat.title");
    public static final Component PARTY_MEMBERS_TITLE = Component.translatable("gui."+ Argonauts.MOD_ID +".party_members.title");
    public static final Component PARTY_SETTINGS_TITLE = Component.translatable("gui."+ Argonauts.MOD_ID +".party_settings.title");

    public static final Component GUILD_CHAT_TITLE = Component.translatable("gui."+ Argonauts.MOD_ID +".guild_chat.title");
    public static final Component GUILD_MEMBERS_TITLE = Component.translatable("gui."+ Argonauts.MOD_ID +".guild_members.title");
    public static final Component GUILD_SETTINGS_TITLE = Component.translatable("gui."+ Argonauts.MOD_ID +".guild_settings.title");

    public static final Component MEMBER_STATUS = Component.translatable("gui."+ Argonauts.MOD_ID +".member_status");
    public static final Component MEMBER_PERMISSIONS = Component.translatable("gui."+ Argonauts.MOD_ID +".member_permissions");
    public static final Component MEMBER_ACTIONS = Component.translatable("gui."+ Argonauts.MOD_ID +".member_actions");
    public static final Component SETTINGS = Component.translatable("gui."+ Argonauts.MOD_ID +".settings");

    public static final Component REMOVE_MEMBER = Component.translatable("gui."+ Argonauts.MOD_ID +".remove_member");
    public static final Component REMOVE = Component.translatable("gui."+ Argonauts.MOD_ID +".remove");

    public static final Component MAX_GUILD_MEMBERS = Component.translatable("gui."+ Argonauts.MOD_ID +".max_guild_members");
    public static final Component MAX_PARTY_MEMBERS = Component.translatable("gui."+ Argonauts.MOD_ID +".max_party_members");

    public static final Component FRIEND_LIST = CommonUtils.serverTranslatable("command."+ Argonauts.MOD_ID +".list_friends");
    public static final Component FRIENDS = Component.translatable("gui."+ Argonauts.MOD_ID +".friends");

    public static final Component ONLINE = Component.translatable("gui."+ Argonauts.MOD_ID +".online");
    public static final Component OFFLINE = Component.translatable("gui."+ Argonauts.MOD_ID +".offline");
}
