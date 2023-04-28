package earth.terrarium.argonauts.common.handlers.party.members;

public enum PartyMemberState {
    LEADER,
    MEMBER,
    INVITED;

    public boolean isLeader() {
        return this == LEADER;
    }
}
