package earth.terrarium.argonauts.common.handlers.base;

import net.minecraft.network.chat.Component;

public class MemberException extends Exception {

    public static final MemberException NO_PERMISSIONS = new MemberException(Component.translatable("command.argonauts.exception.no_permissions"));
    public static final MemberException YOU_CANT_GIVE_PERMISSIONS = new MemberException(Component.translatable("command.argonauts.exception.you_cant_give_permissions"));
    public static final MemberException MEMBER_HAS_PASSIVE_TP_DISABLED = new MemberException(Component.translatable("command.argonauts.exception.member_has_passive_tp_disabled"));
    public static final MemberException HQ_NOT_SET = new MemberException(Component.translatable("command.argonauts.exception.hq_not_set"));
    public static final MemberException YOU_CANT_INVITE_YOURSELF = new MemberException(Component.translatable("command.argonauts.exception.you_cant_invite_yourself"));

    public static final MemberException ALREADY_IN_PARTY = new MemberException(Component.translatable("command.argonauts.exception.already_in_party"));
    public static final MemberException PLAYER_ALREADY_IN_PARTY = new MemberException(Component.translatable("command.argonauts.exception.player_already_in_party"));
    public static final MemberException YOU_ARE_NOT_IN_PARTY = new MemberException(Component.translatable("command.argonauts.exception.you_are_not_in_party"));
    public static final MemberException PLAYER_IS_NOT_IN_PARTY = new MemberException(Component.translatable("command.argonauts.exception.player_is_not_in_party"));
    public static final MemberException PARTY_DOES_NOT_EXIST = new MemberException(Component.translatable("command.argonauts.exception.party_does_not_exist"));
    public static final MemberException NOT_ALLOWED_TO_JOIN_PARTY = new MemberException(Component.translatable("command.argonauts.exception.not_allowed_to_join_party"));
    public static final MemberException YOU_ARE_NOT_IN_THIS_PARTY = new MemberException(Component.translatable("command.argonauts.exception.you_are_not_in_this_party"));
    public static final MemberException YOU_CANT_MANAGE_MEMBERS_IN_PARTY = new MemberException(Component.translatable("command.argonauts.exception.you_cant_manage_members_in_party"));
    public static final MemberException YOU_CANT_TP_MEMBERS_IN_PARTY = new MemberException(Component.translatable("command.argonauts.exception.you_cant_tp_members_in_party"));
    public static final MemberException YOU_ARE_NOT_THE_LEADER_OF_PARTY = new MemberException(Component.translatable("command.argonauts.exception.you_are_not_the_leader_of_party"));
    public static final MemberException NOT_IN_SAME_PARTY = new MemberException(Component.translatable("command.argonauts.exception.not_in_same_party"));
    public static final MemberException YOU_CANT_REMOVE_PARTY_LEADER = new MemberException(Component.translatable("command.argonauts.exception.you_cant_remove_party_leader"));
    public static final MemberException YOU_CANT_REMOVE_YOURSELF_FROM_PARTY = new MemberException(Component.translatable("command.argonauts.exception.you_cant_remove_yourself_from_party"));
    public static final MemberException PARTY_HAS_PASSIVE_TP_DISABLED = new MemberException(Component.translatable("command.argonauts.exception.party_has_passive_tp_disabled"));
    public static final MemberException YOU_CANT_SET_LEADER_TO_NON_PARTY_MEMBER = new MemberException(Component.translatable("command.argonauts.exception.you_cant_set_leader_to_non_party_member"));
    public static final MemberException YOU_CANT_SET_LEADER_TO_CURRENT_LEADER = new MemberException(Component.translatable("command.argonauts.exception.you_cant_set_leader_to_current_leader"));
    public static final MemberException THERE_ARE_NO_GUILDS = new MemberException(Component.translatable("command.argonauts.exception.there_are_no_guilds"));

    public static final MemberException ALREADY_IN_GUILD = new MemberException(Component.translatable("command.argonauts.exception.already_in_guild"));
    public static final MemberException PLAYER_ALREADY_IN_GUILD = new MemberException(Component.translatable("command.argonauts.exception.player_already_in_guild"));
    public static final MemberException YOU_ARE_NOT_IN_GUILD = new MemberException(Component.translatable("command.argonauts.exception.you_are_not_in_guild"));
    public static final MemberException PLAYER_IS_NOT_IN_GUILD = new MemberException(Component.translatable("command.argonauts.exception.player_is_not_in_guild"));
    public static final MemberException GUILD_DOES_NOT_EXIST = new MemberException(Component.translatable("command.argonauts.exception.guild_does_not_exist"));
    public static final MemberException NOT_ALLOWED_TO_JOIN_GUILD = new MemberException(Component.translatable("command.argonauts.exception.not_allowed_to_join_guild"));
    public static final MemberException YOU_ARE_NOT_IN_THIS_GUILD = new MemberException(Component.translatable("command.argonauts.exception.you_are_not_in_this_guild"));
    public static final MemberException YOU_CANT_MANAGE_MEMBERS_IN_GUILD = new MemberException(Component.translatable("command.argonauts.exception.you_cant_manage_members_in_guild"));
    public static final MemberException YOU_ARE_NOT_THE_OWNER_OF_GUILD = new MemberException(Component.translatable("command.argonauts.exception.you_are_not_the_owner_of_guild"));
    public static final MemberException YOU_CANT_REMOVE_GUILD_OWNER = new MemberException(Component.translatable("command.argonauts.exception.you_cant_remove_guild_owner"));
    public static final MemberException YOU_CANT_REMOVE_YOURSELF_FROM_GUILD = new MemberException(Component.translatable("command.argonauts.exception.you_cant_remove_yourself_from_guild"));
    public static final MemberException YOU_CANT_SET_OWNER_TO_NON_GUILD_MEMBER = new MemberException(Component.translatable("command.argonauts.exception.you_cant_set_owner_to_non_guild_member"));
    public static final MemberException YOU_CANT_SET_OWNER_TO_CURRENT_OWNER = new MemberException(Component.translatable("command.argonauts.exception.you_cant_set_owner_to_current_owner"));

    private final Component message;

    private MemberException(Component message) {
        super(message.getString());
        this.message = message;
    }

    public Component message() {
        return message;
    }

}
