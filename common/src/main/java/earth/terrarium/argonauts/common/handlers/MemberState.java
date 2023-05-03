package earth.terrarium.argonauts.common.handlers;

public enum MemberState {
    OWNER,
    MEMBER,
    INVITED;

    public boolean isLeader() {
        return this == OWNER;
    }
}
