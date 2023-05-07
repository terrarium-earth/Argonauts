package earth.terrarium.argonauts.common.handlers.base;

import java.util.List;

public final class MemberPermissions {

    public static final String MANAGE_MEMBERS = "manage.members";
    public static final String MANAGE_ROLES = "roles.members";
    public static final String MANAGE_PERMISSIONS = "permissions.members";
    public static final String MANAGE_SETTINGS = "manage.settings";
    public static final String TP_MEMBERS = "tp.members";

    public static final List<String> ALL_PERMISSIONS = List.of(
        MANAGE_MEMBERS,
        MANAGE_ROLES,
        MANAGE_PERMISSIONS,
        MANAGE_SETTINGS,
        TP_MEMBERS
    );
}
