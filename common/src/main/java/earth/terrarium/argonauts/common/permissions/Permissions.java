package earth.terrarium.argonauts.common.permissions;

import earth.terrarium.argonauts.api.teams.permissions.MemberPermissionsApi;

public class Permissions {

    public static final String OPERATOR = "operator";
    public static final String MANAGE_MEMBERS = "manageMembers";
    public static final String MANAGE_SETTINGS = "manageSettings";
    public static final String MANAGE_PERMISSIONS = "managePermissions";
    public static final String TELEPORT = "teleport";

    public static final String TELEPORT_MEMBERS = "teleportMembers";

    public static void init() {
        MemberPermissionsApi.API.register(OPERATOR, false);
        MemberPermissionsApi.API.register(MANAGE_MEMBERS, false);
        MemberPermissionsApi.API.register(MANAGE_SETTINGS, false);
        MemberPermissionsApi.API.register(MANAGE_PERMISSIONS, false);
        MemberPermissionsApi.API.register(TELEPORT, true);

        MemberPermissionsApi.API.registerPartyPermission(TELEPORT_MEMBERS, false);
    }
}
