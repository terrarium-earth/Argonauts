package earth.terrarium.argonauts.common.handlers.base.members;

public enum MemberState {
    OWNER,
    MEMBER,
    INVITED,
    ALLIED,
    ;

    public boolean isLeader() {
        return this == OWNER;
    }

    public boolean isPermanentMember() {
        return this == MEMBER || isLeader();
    }
}
