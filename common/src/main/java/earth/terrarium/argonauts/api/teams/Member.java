package earth.terrarium.argonauts.api.teams;

import com.teamresourceful.bytecodecs.base.ByteCodec;
import com.teamresourceful.bytecodecs.base.object.ObjectByteCodec;
import com.teamresourceful.bytecodecs.defaults.MapCodec;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;

import java.util.Objects;

public class Member {

    public static final ByteCodec<Member> BYTE_CODEC = ObjectByteCodec.create(
        MemberStatus.BYTE_CODEC.fieldOf(Member::status),
        new MapCodec<>(ByteCodec.STRING, ByteCodec.BOOLEAN)
            .map(map -> (Object2BooleanMap<String>) new Object2BooleanOpenHashMap<>(map), map -> map
            ).fieldOf(Member::permissions),
        Member::new
    );

    private MemberStatus status;
    private final Object2BooleanMap<String> permissions;

    public Member(MemberStatus status, Object2BooleanMap<String> permissions) {
        this.status = status;
        this.permissions = new Object2BooleanOpenHashMap<>(permissions);
    }

    public boolean isOwner() {
        return this.status.isOwner();
    }

    public MemberStatus status() {
        return this.status;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    public Object2BooleanMap<String> permissions() {
        return this.permissions;
    }

    public boolean hasPermission(String permission) {
        return this.permissions.getBoolean(permission);
    }

    public void setPermission(String permission, boolean value) {
        this.permissions.put(permission, value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (Member) obj;
        return Objects.equals(this.status, that.status) &&
            Objects.equals(this.permissions, that.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status, permissions);
    }

    @Override
    public String toString() {
        return "Member[" +
            "status=" + status + ", " +
            "permissions=" + permissions + ']';
    }
}
