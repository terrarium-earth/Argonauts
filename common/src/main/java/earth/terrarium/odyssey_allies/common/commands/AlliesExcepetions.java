package earth.terrarium.odyssey_allies.common.commands;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.teamresourceful.resourcefullib.common.utils.CommonUtils;

public class AlliesExcepetions {

    public static final SimpleCommandExceptionType ALREADY_IN_GUILD = create("command.odyssey_allies.exception.already_in_guild");
    public static final SimpleCommandExceptionType NOT_IN_GUILD = create("command.odyssey_allies.exception.not_in_guild");
    public static final SimpleCommandExceptionType PLAYER_NOT_IN_GUILD = create("command.odyssey_allies.exception.player_not_in_guild");
    public static final SimpleCommandExceptionType PLAYER_IS_GUILD_MEMBER = create("command.odyssey_allies.exception.player_is_guild_member");
    public static final SimpleCommandExceptionType NOT_GUILD_OWNER = create("command.odyssey_allies.exception.not_guild_owner");
    public static final SimpleCommandExceptionType HEADQUARTERS_NOT_SET = create("command.odyssey_allies.exception.headquarters_not_set");
    public static final SimpleCommandExceptionType CANT_ALLY_YOURSELF = create("command.odyssey_allies.exception.cant_ally_yourself");
    public static final SimpleCommandExceptionType NOT_ALLY = create("command.odyssey_allies.exception.not_ally");
    public static final SimpleCommandExceptionType PLAYER_ALREADY_ALLY = create("command.odyssey_allies.exception.player_already_ally");
    public static final SimpleCommandExceptionType NOT_INVITED_TO_GUILD = create("command.odyssey_allies.exception.not_invited_to_guild");
    public static final SimpleCommandExceptionType GUILD_FULL = create("command.odyssey_allies.exception.guild_full");
    public static final SimpleCommandExceptionType FAKE_PLAYER_ALREADY_IN_GUILD = create("command.odyssey_allies.exception.fake_player_already_in_guild");
    public static final SimpleCommandExceptionType FAKE_PLAYER_NOT_IN_GUILD = create("command.odyssey_allies.exception.fake_player_not_in_guild");
    public static final SimpleCommandExceptionType GUILD_DOES_NOT_EXIST = create("command.odyssey_allies.exception.guild_does_not_exist");

    public static final SimpleCommandExceptionType ALREADY_IN_PARTY = create("command.odyssey_allies.exception.already_in_party");
    public static final SimpleCommandExceptionType NOT_IN_PARTY = create("command.odyssey_allies.exception.not_in_party");
    public static final SimpleCommandExceptionType PLAYER_NOT_IN_PARTY = create("command.odyssey_allies.exception.player_not_in_party");
    public static final SimpleCommandExceptionType PLAYER_IS_PARTY_MEMBER = create("command.odyssey_allies.exception.player_is_party_member");
    public static final SimpleCommandExceptionType NOT_PARTY_OWNER = create("command.odyssey_allies.exception.not_party_owner");
    public static final SimpleCommandExceptionType NOT_INVITED_TO_PARTY = create("command.odyssey_allies.exception.not_invited_to_party");
    public static final SimpleCommandExceptionType PARTY_FULL = create("command.odyssey_allies.exception.party_full");

    public static final SimpleCommandExceptionType CANT_TRANSFER_TO_YOURSELF = create("command.odyssey_allies.exception.cant_transfer_to_yourself");
    public static final SimpleCommandExceptionType CANT_INVITE_YOURSELF = create("command.odyssey_allies.exception.cant_invite_yourself");
    public static final SimpleCommandExceptionType CANT_KICK_YOURSELF = create("command.odyssey_allies.exception.cant_kick_yourself");

    public static final SimpleCommandExceptionType NO_PERMISSION_MANAGE_MEMBERS = create("command.odyssey_allies.exception.no_permission_manage_members");
    public static final SimpleCommandExceptionType NO_PERMISSION_MANAGE_SETTINGS = create("command.odyssey_allies.exception.no_permission_manage_settings");
    public static final SimpleCommandExceptionType NO_PERMISSION_MANAGE_PERMISSIONS = create("command.odyssey_allies.exception.no_permission_manage_permissions");
    public static final SimpleCommandExceptionType NO_PERMISSION_TELEPORT = create("command.odyssey_allies.exception.no_permission_teleport");
    public static final SimpleCommandExceptionType NO_PERMISSION_TELEPORT_MEMBERS = create("command.odyssey_allies.exception.no_permission_teleport_members");
    public static final SimpleCommandExceptionType NO_PERMISSION_CREATE_GUILD = create("command.odyssey_allies.exception.no_permission_create_guild");
    public static final SimpleCommandExceptionType NO_PERMISSION_CREATE_PARTY = create("command.odyssey_allies.exception.no_permission_create_party");
    public static final SimpleCommandExceptionType PASSIVE_TELEPORT_DISABLED = create("command.odyssey_allies.exception.passive_teleport_disabled");

    public static final SimpleCommandExceptionType ALREADY_FRIENDS = create("command.odyssey_allies.exception.already_friends");
    public static final SimpleCommandExceptionType ALREADY_SENT_FRIEND_REQUEST = create("command.odyssey_allies.exception.already_sent_friend_request");
    public static final SimpleCommandExceptionType NO_PENDING_REQUEST = create("command.odyssey_allies.exception.no_pending_request");

    private static SimpleCommandExceptionType create(String translationKey) {
        return new SimpleCommandExceptionType(CommonUtils.serverTranslatable(translationKey));
    }
}
