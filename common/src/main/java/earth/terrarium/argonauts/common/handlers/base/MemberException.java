package earth.terrarium.argonauts.common.handlers.base;

import net.minecraft.network.chat.Component;

public class MemberException extends Exception {

    public static final MemberException NO_PERMISSIONS = new MemberException(Component.literal("You don't have the required permissions."));
    public static final MemberException YOU_CANT_GIVE_PERMISSIONS = new MemberException(Component.literal("You can't give permissions you don't have."));
    public static final MemberException MEMBER_HAS_PASSIVE_TP_DISABLED = new MemberException(Component.literal("The member has passive teleportation disabled."));

    public static final MemberException ALREADY_IN_PARTY = new MemberException(Component.literal("You are already in a party."));
    public static final MemberException YOU_ARE_NOT_IN_PARTY = new MemberException(Component.literal("You are not in a party."));
    public static final MemberException PLAYER_IS_NOT_IN_PARTY = new MemberException(Component.literal("The player is not in a party."));
    public static final MemberException PARTY_IS_NOT_PUBLIC = new MemberException(Component.literal("The party is not public."));
    public static final MemberException PARTY_DOES_NOT_EXIST = new MemberException(Component.literal("The party does not exist."));
    public static final MemberException NOT_ALLOWED_TO_JOIN_PARTY = new MemberException(Component.literal("You are not allowed to join this party."));
    public static final MemberException YOU_ARE_NOT_IN_THIS_PARTY = new MemberException(Component.literal("You are not in this party."));
    public static final MemberException YOU_CANT_MANAGE_MEMBERS_IN_PARTY = new MemberException(Component.literal("You can't manage members in this party."));
    public static final MemberException YOU_CANT_TP_MEMBERS_IN_PARTY = new MemberException(Component.literal("You can't teleport members in this party."));
    public static final MemberException YOU_ARE_NOT_THE_LEADER_OF_PARTY = new MemberException(Component.literal("You are not the leader of this party."));
    public static final MemberException NOT_IN_SAME_PARTY = new MemberException(Component.literal("You are not in the same party."));
    public static final MemberException CANT_REMOVE_PARTY_LEADER = new MemberException(Component.literal("You can't remove the leader of the party."));
    public static final MemberException YOU_CANT_REMOVE_YOURSELF_FROM_PARTY = new MemberException(Component.literal("You can't remove yourself from the party."));
    public static final MemberException PARTY_HAS_PASSIVE_TP_DISABLED = new MemberException(Component.literal("The party has passive teleportation disabled."));

    public static final MemberException ALREADY_IN_GUILD = new MemberException(Component.literal("You are already in a guild."));
    public static final MemberException YOU_ARE_NOT_IN_GUILD = new MemberException(Component.literal("You are not in a guild."));
    public static final MemberException PLAYER_IS_NOT_IN_GUILD = new MemberException(Component.literal("The player is not in a guild."));
    public static final MemberException GUILD_IS_NOT_PUBLIC = new MemberException(Component.literal("The guild is not public."));
    public static final MemberException GUILD_DOES_NOT_EXIST = new MemberException(Component.literal("The guild does not exist."));
    public static final MemberException NOT_ALLOWED_TO_JOIN_GUILD = new MemberException(Component.literal("You are not allowed to join this guild."));
    public static final MemberException YOU_ARE_NOT_IN_THIS_GUILD = new MemberException(Component.literal("You are not in this guild."));
    public static final MemberException YOU_CANT_MANAGE_MEMBERS_IN_GUILD = new MemberException(Component.literal("You can't manage members in this guild."));
    public static final MemberException YOU_CANT_TP_MEMBERS_IN_GUILD = new MemberException(Component.literal("You can't teleport members in this guild."));
    public static final MemberException YOU_ARE_NOT_THE_OWNER_OF_GUILD = new MemberException(Component.literal("You are not the owner of this guild."));
    public static final MemberException NOT_IN_SAME_GUILD = new MemberException(Component.literal("You are not in the same guild."));
    public static final MemberException CANT_REMOVE_GUILD_OWNER = new MemberException(Component.literal("You can't remove the owner of the guild."));
    public static final MemberException YOU_CANT_REMOVE_YOURSELF_FROM_GUILD = new MemberException(Component.literal("You can't remove yourself from the guild."));
    public static final MemberException GUILD_HAS_PASSIVE_TP_DISABLED = new MemberException(Component.literal("The guild has passive teleportation disabled."));


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
