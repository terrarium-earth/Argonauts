package earth.terrarium.argonauts.common.handlers.base;

import net.minecraft.network.chat.Component;

public class MemberException extends Exception {

    public static final MemberException NO_PERMISSIONS = new MemberException(Component.translatable("command.argonauts.exception.no_permissions"));
    public static final MemberException YOU_CANT_GIVE_PERMISSIONS = new MemberException(Component.translatable("command.argonauts.exception.you_cant_give_permissions"));
    public static final MemberException MEMBER_HAS_PASSIVE_TP_DISABLED = new MemberException(Component.translatable("command.argonauts.exception.member_has_passive_tp_disabled"));

    public static final MemberException ALREADY_IN_PARTY = new MemberException(Component.translatable("command.argonauts.exception.already_in_party"));
    public static final MemberException YOU_ARE_NOT_IN_PARTY = new MemberException(Component.translatable("command.argonauts.exception.you_are_not_in_party"));
    public static final MemberException PLAYER_IS_NOT_IN_PARTY = new MemberException(Component.translatable("command.argonauts.exception.player_is_not_in_party"));
    public static final MemberException PARTY_IS_NOT_PUBLIC = new MemberException(Component.translatable("command.argonauts.exception.party_is_not_public"));
    public static final MemberException PARTY_DOES_NOT_EXIST = new MemberException(Component.translatable("command.argonauts.exception.party_does_not_exist"));
    public static final MemberException NOT_ALLOWED_TO_JOIN_PARTY = new MemberException(Component.translatable("command.argonauts.exception.not_allowed_to_join_party"));
    public static final MemberException YOU_ARE_NOT_IN_THIS_PARTY = new MemberException(Component.translatable("command.argonauts.exception.you_are_not_in_this_party"));
    public static final MemberException YOU_CANT_MANAGE_MEMBERS_IN_PARTY = new MemberException(Component.translatable("command.argonauts.exception.you_cant_manage_members_in_party"));
    public static final MemberException YOU_CANT_TP_MEMBERS_IN_PARTY = new MemberException(Component.translatable("command.argonauts.exception.you_cant_tp_members_in_party"));
    public static final MemberException YOU_ARE_NOT_THE_LEADER_OF_PARTY = new MemberException(Component.translatable("command.argonauts.exception.you_are_not_the_leader_of_party"));
    public static final MemberException NOT_IN_SAME_PARTY = new MemberException(Component.translatable("command.argonauts.exception.not_in_same_party"));
    public static final MemberException CANT_REMOVE_PARTY_LEADER = new MemberException(Component.translatable("command.argonauts.exception.cant_remove_party_leader"));
    public static final MemberException YOU_CANT_REMOVE_YOURSELF_FROM_PARTY = new MemberException(Component.translatable("command.argonauts.exception.you_cant_remove_yourself_from_party"));
    public static final MemberException PARTY_HAS_PASSIVE_TP_DISABLED = new MemberException(Component.translatable("command.argonauts.exception.party_has_passive_tp_disabled"));

    public static final MemberException ALREADY_IN_GUILD = new MemberException(Component.translatable("command.argonauts.exception.already_in_guild"));
    public static final MemberException YOU_ARE_NOT_IN_GUILD = new MemberException(Component.translatable("command.argonauts.exception.you_are_not_in_guild"));
    public static final MemberException PLAYER_IS_NOT_IN_GUILD = new MemberException(Component.translatable("command.argonauts.exception.player_is_not_in_guild"));
    public static final MemberException GUILD_IS_NOT_PUBLIC = new MemberException(Component.translatable("command.argonauts.exception.guild_is_not_public"));
    public static final MemberException GUILD_DOES_NOT_EXIST = new MemberException(Component.translatable("command.argonauts.exception.guild_does_not_exist"));
    public static final MemberException NOT_ALLOWED_TO_JOIN_GUILD = new MemberException(Component.translatable("command.argonauts.exception.not_allowed_to_join_guild"));
    public static final MemberException YOU_ARE_NOT_IN_THIS_GUILD = new MemberException(Component.translatable("command.argonauts.exception.you_are_not_in_this_guild"));
    public static final MemberException YOU_CANT_MANAGE_MEMBERS_IN_GUILD = new MemberException(Component.translatable("command.argonauts.exception.you_cant_manage_members_in_guild"));
    public static final MemberException YOU_CANT_TP_MEMBERS_IN_GUILD = new MemberException(Component.translatable("command.argonauts.exception.you_cant_tp_members_in_guild"));
    public static final MemberException YOU_ARE_NOT_THE_OWNER_OF_GUILD = new MemberException(Component.translatable("command.argonauts.exception.you_are_not_the_owner_of_guild"));
    public static final MemberException NOT_IN_SAME_GUILD = new MemberException(Component.translatable("command.argonauts.exception.not_in_same_guild"));
    public static final MemberException CANT_REMOVE_GUILD_OWNER = new MemberException(Component.translatable("command.argonauts.exception.cant_remove_guild_owner"));
    public static final MemberException YOU_CANT_REMOVE_YOURSELF_FROM_GUILD = new MemberException(Component.translatable("command.argonauts.exception.you_cant_remove_yourself_from_guild"));
    public static final MemberException GUILD_HAS_PASSIVE_TP_DISABLED = new MemberException(Component.translatable("command.argonauts.exception.guild_has_passive_tp_disabled"));


    private final Component message;

    public MemberException(String message) {
        super(message);
        this.message = Component.literal(message);
    }

    public MemberException(Component message) {
        super(message.getString());
        this.message = message;
    }

    public Component message() {
        return message;
    }

}
