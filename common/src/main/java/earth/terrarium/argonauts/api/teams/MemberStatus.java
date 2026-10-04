package earth.terrarium.argonauts.api.teams;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.resourcefullib.common.utils.CommonUtils;
import net.minecraft.network.chat.Component;

public enum MemberStatus {
    OWNER,
    MEMBER,
    INVITED,
    ALLIED,
    FAKE_PLAYER;

    public boolean isOwner() {
        return this == OWNER;
    }

    public boolean isMember() {
        return this == MEMBER || isOwner();
    }

    public boolean isInvited() {
        return this == INVITED;
    }

    public boolean isAllied() {
        return this == ALLIED;
    }

    public boolean isFakePlayer() {
        return this == FAKE_PLAYER;
    }

    public Component getDisplayName() {
        return CommonUtils.serverTranslatable("team.argonauts.member_status." + this.name().toLowerCase());
    }

    public static final ByteCodec<MemberStatus> BYTE_CODEC = ByteCodec.ofEnum(MemberStatus.class);
}
