package earth.terrarium.argonauts.api.teams.permissions;

import earth.terrarium.argonauts.api.ApiHelper;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import net.minecraft.network.chat.Component;

public interface MemberPermissionsApi {

    MemberPermissionsApi API = ApiHelper.load(MemberPermissionsApi.class);

    /**
     * Registers a new party permission with a default value.
     *
     * @param name         The name of the permission.
     * @param defaultValue The value of the permission.
     */
    void registerPartyPermission(String name, boolean defaultValue);

    /**
     * Registers a new guild permission with a default value.
     *
     * @param name         The name of the permission.
     * @param defaultValue The value of the permission.
     */
    void registerGuildPermission(String name, boolean defaultValue);

    /**
     * Registers a new party and guild permission with a default value.
     *
     * @param name         The name of the permission.
     * @param defaultValue The value of the permission.
     */
    default void register(String name, boolean defaultValue) {
        registerPartyPermission(name, defaultValue);
        registerGuildPermission(name, defaultValue);
    }

    /**
     * Gets all permissions that are available to parties.
     *
     * @return All party permissions.
     */
    Object2BooleanMap<String> getPartyPermissions();

    /**
     * Gets all permissions that are available to guilds.
     *
     * @return All guild permissions.
     */
    Object2BooleanMap<String> getGuildPermissions();

    /**
     * Creates the default guild permissions for a member.
     *
     * @return The default permissions.
     */
    Object2BooleanMap<String> createDefaultGuildPermissions();

    /**
     * Creates the default party permissions for a member.
     *
     * @return The default permissions.
     */
    Object2BooleanMap<String> createDefaultPartyPermissions();

    /**
     * Gets the translated name of the permission.
     *
     * @param permission The permission.
     * @return The name of the permission.
     */
    Component getPermissionName(String permission);
}
