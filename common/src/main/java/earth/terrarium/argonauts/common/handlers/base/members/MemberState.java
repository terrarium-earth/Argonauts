package earth.terrarium.argonauts.common.handlers.base.members;

public enum MemberState {
    OWNER,
    MEMBER,
    INVITED;

    public boolean isLeader() {
        return this == OWNER;
    }
}
