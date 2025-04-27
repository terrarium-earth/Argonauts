package earth.terrarium.argonauts.common.commands;

import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.teamresourceful.resourcefullib.common.utils.CommonUtils;

public class TeamExceptions {

    public static final SimpleCommandExceptionType ALREADY_IN_GUILD = create("command.argonauts.exception.already_in_guild");
    public static final SimpleCommandExceptionType NOT_IN_GUILD = create("command.argonauts.exception.not_in_guild");
    public static final SimpleCommandExceptionType PLAYER_NOT_IN_GUILD = create("command.argonauts.exception.player_not_in_guild");
    public static final SimpleCommandExceptionType PLAYER_IS_GUILD_MEMBER = create("command.argonauts.exception.player_is_guild_member");
    public static final SimpleCommandExceptionType NOT_GUILD_OWNER = create("command.argonauts.exception.not_guild_owner");
    public static final SimpleCommandExceptionType HEADQUARTERS_NOT_SET = create("command.argonauts.exception.headquarters_not_set");
    public static final SimpleCommandExceptionType CANT_ALLY_YOURSELF = create("command.argonauts.exception.cant_ally_yourself");
    public static final SimpleCommandExceptionType NOT_ALLY = create("command.argonauts.exception.not_ally");
    public static final SimpleCommandExceptionType PLAYER_ALREADY_ALLY = create("command.argonauts.exception.player_already_ally");
    public static final SimpleCommandExceptionType NOT_INVITED_TO_GUILD = create("command.argonauts.exception.not_invited_to_guild");
    public static final SimpleCommandExceptionType GUILD_FULL = create("command.argonauts.exception.guild_full");
    public static final SimpleCommandExceptionType FAKE_PLAYER_ALREADY_IN_GUILD = create("command.argonauts.exception.fake_player_already_in_guild");
    public static final SimpleCommandExceptionType FAKE_PLAYER_NOT_IN_GUILD = create("command.argonauts.exception.fake_player_not_in_guild");
    public static final SimpleCommandExceptionType GUILD_DOES_NOT_EXIST = create("command.argonauts.exception.guild_does_not_exist");

    public static final SimpleCommandExceptionType ALREADY_IN_PARTY = create("command.argonauts.exception.already_in_party");
    public static final SimpleCommandExceptionType NOT_IN_PARTY = create("command.argonauts.exception.not_in_party");
    public static final SimpleCommandExceptionType PLAYER_NOT_IN_PARTY = create("command.argonauts.exception.player_not_in_party");
    public static final SimpleCommandExceptionType PLAYER_IS_PARTY_MEMBER = create("command.argonauts.exception.player_is_party_member");
    public static final SimpleCommandExceptionType NOT_PARTY_OWNER = create("command.argonauts.exception.not_party_owner");
    public static final SimpleCommandExceptionType NOT_INVITED_TO_PARTY = create("command.argonauts.exception.not_invited_to_party");
    public static final SimpleCommandExceptionType PARTY_FULL = create("command.argonauts.exception.party_full");

    public static final SimpleCommandExceptionType CANT_TRANSFER_TO_YOURSELF = create("command.argonauts.exception.cant_transfer_to_yourself");
    public static final SimpleCommandExceptionType CANT_INVITE_YOURSELF = create("command.argonauts.exception.cant_invite_yourself");
    public static final SimpleCommandExceptionType CANT_KICK_YOURSELF = create("command.argonauts.exception.cant_kick_yourself");

    public static final SimpleCommandExceptionType NO_PERMISSION_MANAGE_MEMBERS = create("command.argonauts.exception.no_permission_manage_members");
    public static final SimpleCommandExceptionType NO_PERMISSION_MANAGE_SETTINGS = create("command.argonauts.exception.no_permission_manage_settings");
    public static final SimpleCommandExceptionType NO_PERMISSION_MANAGE_PERMISSIONS = create("command.argonauts.exception.no_permission_manage_permissions");
    public static final SimpleCommandExceptionType NO_PERMISSION_TELEPORT = create("command.argonauts.exception.no_permission_teleport");
    public static final SimpleCommandExceptionType NO_PERMISSION_TELEPORT_MEMBERS = create("command.argonauts.exception.no_permission_teleport_members");
    public static final SimpleCommandExceptionType NO_PERMISSION_CREATE_GUILD = create("command.argonauts.exception.no_permission_create_guild");
    public static final SimpleCommandExceptionType NO_PERMISSION_CREATE_PARTY = create("command.argonauts.exception.no_permission_create_party");
    public static final SimpleCommandExceptionType PASSIVE_TELEPORT_DISABLED = create("command.argonauts.exception.passive_teleport_disabled");

    private static SimpleCommandExceptionType create(String translationKey) {
        return new SimpleCommandExceptionType(CommonUtils.serverTranslatable(translationKey));
    }
}
