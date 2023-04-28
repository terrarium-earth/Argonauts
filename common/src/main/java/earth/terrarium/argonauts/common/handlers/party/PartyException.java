package earth.terrarium.argonauts.common.handlers.party;

import net.minecraft.network.chat.Component;

public class PartyException extends Exception {

    public static final PartyException ALREADY_IN_PARTY = new PartyException(Component.literal("You are already in a party."));
    public static final PartyException YOU_ARE_NOT_IN_PARTY = new PartyException(Component.literal("You are not in a party."));
    public static final PartyException PLAYER_IS_NOT_IN_PARTY = new PartyException(Component.literal("The player is not in a party."));
    public static final PartyException PARTY_IS_NOT_PUBLIC = new PartyException(Component.literal("The party is not public."));
    public static final PartyException PARTY_DOES_NOT_EXIST = new PartyException(Component.literal("The party does not exist."));
    public static final PartyException NOT_ALLOWED_TO_JOIN_PARTY = new PartyException(Component.literal("You are not allowed to join this party."));
    public static final PartyException YOU_ARE_NOT_IN_THIS_PARTY = new PartyException(Component.literal("You are not in this party."));
    public static final PartyException YOU_CANT_MANAGE_MEMBERS = new PartyException(Component.literal("You can't manage members in this party."));
    public static final PartyException YOU_CANT_TP_MEMBERS = new PartyException(Component.literal("You can't teleport members in this party."));
    public static final PartyException YOU_ARE_NOT_THE_LEADER = new PartyException(Component.literal("You are not the leader of this party."));
    public static final PartyException NOT_IN_SAME_PARTY = new PartyException(Component.literal("You are not in the same party."));
    public static final PartyException CANT_REMOVE_LEADER = new PartyException(Component.literal("You can't remove the leader of the party."));
    public static final PartyException NO_PERMISSIONS = new PartyException(Component.literal("You don't have the required permissions."));
    public static final PartyException YOU_CANT_GIVE_PERMISSIONS = new PartyException(Component.literal("You can't give permissions you don't have."));
    public static final PartyException YOU_CANT_REMOVE_YOURSELF = new PartyException(Component.literal("You can't remove yourself from the party."));
    public static final PartyException PARTY_HAS_PASSIVE_TP_DISABLED = new PartyException(Component.literal("The party has passive teleportation disabled."));
    public static final PartyException MEMBER_HAS_PASSIVE_TP_DISABLED = new PartyException(Component.literal("The member has passive teleportation disabled."));

    private final Component message;

    public PartyException(String message) {
        super(message);
        this.message = Component.literal(message);
    }

    public PartyException(Component message) {
        super(message.getString());
        this.message = message;
    }

    public Component message() {
        return message;
    }

}
