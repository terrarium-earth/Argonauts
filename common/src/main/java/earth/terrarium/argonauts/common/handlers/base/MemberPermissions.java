package earth.terrarium.argonauts.common.handlers.base;

import java.util.List;

public final class MemberPermissions {

    public static final String MANAGE_MEMBERS = "manage.members";
    public static final String MANAGE_ROLES = "roles.members";
    public static final String MANAGE_PERMISSIONS = "permissions.members";
    public static final String MANAGE_SETTINGS = "manage.settings";
    public static final String TP_MEMBERS = "tp.members";

    public static final String TEMPORARY_GUILD_PERMISSIONS = "cadmus.temporary_guild_permissions";

    public static final String BREAK_BLOCKS = "cadmus.blocks.break";
    public static final String PLACE_BLOCKS = "cadmus.blocks.place";
    public static final String EXPLODE_BLOCKS = "cadmus.blocks.explode";
    public static final String INTERACT_WITH_BLOCKS = "cadmus.blocks.interact";
    public static final String INTERACT_WITH_ENTITIES = "cadmus.entities.interact";
    public static final String DAMAGE_ENTITIES = "cadmus.entities.damage";

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
        INTERACT_WITH_BLOCKS,
        INTERACT_WITH_ENTITIES,
        DAMAGE_ENTITIES);
}
