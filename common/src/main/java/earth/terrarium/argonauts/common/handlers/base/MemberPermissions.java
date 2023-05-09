package earth.terrarium.argonauts.common.handlers.base;

import java.util.List;

public final class MemberPermissions {

    public static final String MANAGE_MEMBERS = "manage.members";
    public static final String MANAGE_ROLES = "roles.members";
    public static final String MANAGE_PERMISSIONS = "permissions.members";
    public static final String MANAGE_SETTINGS = "manage.settings";
    public static final String TP_MEMBERS = "tp.members";

    public static final String TEMPORARY_GUILD_PERMISSIONS = "cadmus.temporary_guild_permissions";

    public static final String BREAK_BLOCKS = "cadmus.break_blocks";
    public static final String PLACE_BLOCKS = "cadmus.place_blocks";
    public static final String EXPLODE_BLOCKS = "cadmus.explode_blocks";
    public static final String INTERACT_WITH_BLOCKS = "cadmus.interact_with_blocks";
    public static final String INTERACT_WITH_ENTITIES = "cadmus.interact_with_entities";
    public static final String DAMAGE_ENTITIES = "cadmus.damage_entities";

    public static final List<String> ALL_PERMISSIONS = List.of(
        MANAGE_MEMBERS,
        MANAGE_ROLES,
        MANAGE_PERMISSIONS,
        MANAGE_SETTINGS,
        TP_MEMBERS);

    public static final List<String> CADMUS_PERMISSIONS = List.of(
        BREAK_BLOCKS,
        PLACE_BLOCKS,
        EXPLODE_BLOCKS,
        INTERACT_WITH_ENTITIES,
        DAMAGE_ENTITIES);
}
