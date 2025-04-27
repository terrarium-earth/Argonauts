package earth.terrarium.argonauts.common.permissions;

import com.teamresourceful.resourcefullib.common.utils.CommonUtils;
import earth.terrarium.argonauts.api.teams.permissions.MemberPermissionsApi;
import it.unimi.dsi.fastutil.objects.Object2BooleanMap;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import net.minecraft.network.chat.Component;

public class MemberPermissionsApiImpl implements MemberPermissionsApi {

    private static final Object2BooleanMap<String> PARTY_PERMISSIONS = new Object2BooleanOpenHashMap<>();
    private static final Object2BooleanMap<String> GUILD_PERMISSIONS = new Object2BooleanOpenHashMap<>();

    @Override
    public void registerPartyPermission(String name, boolean defaultValue) {
        PARTY_PERMISSIONS.put(name, defaultValue);
    }

    @Override
    public void registerGuildPermission(String name, boolean defaultValue) {
        GUILD_PERMISSIONS.put(name, defaultValue);
    }

    @Override
    public Object2BooleanMap<String> getPartyPermissions() {
        return PARTY_PERMISSIONS;
    }

    @Override
    public Object2BooleanMap<String> getGuildPermissions() {
        return GUILD_PERMISSIONS;
    }

    @Override
    public Object2BooleanMap<String> createDefaultGuildPermissions() {
        Object2BooleanMap<String> permissions = new Object2BooleanOpenHashMap<>();
        GUILD_PERMISSIONS.forEach((permission, value) -> {
            if (value) permissions.put(permission, true);
        });
        return permissions;
    }

    @Override
    public Object2BooleanMap<String> createDefaultPartyPermissions() {
        Object2BooleanMap<String> permissions = new Object2BooleanOpenHashMap<>();
        PARTY_PERMISSIONS.forEach((permission, value) -> {
            if (value) permissions.put(permission, true);
        });
        return permissions;
    }

    @Override
    public Component getPermissionName(String permission) {
        return CommonUtils.serverTranslatable("permission.argonauts." + permission);
    }
}
