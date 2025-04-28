package earth.terrarium.odyssey_allies.common.compat.roles;

import com.teamresourceful.resourcefullib.common.utils.TriState;
import earth.terrarium.prometheus.api.permissions.PermissionApi;
import earth.terrarium.prometheus.api.roles.RoleApi;
import earth.terrarium.prometheus.api.roles.options.RoleOptionsApi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class RolesCompat {

    public static void init() {
        RoleOptionsApi.API.register(AlliesOptions.SERIALIZER);
        PermissionApi.API.addDefaultPermission(AlliesPermissions.TELEPORT, TriState.TRUE);
        PermissionApi.API.addDefaultPermission(AlliesPermissions.CREATE_PARTY, TriState.TRUE);
        PermissionApi.API.addDefaultPermission(AlliesPermissions.CREATE_GUILD, TriState.TRUE);
    }

    public static boolean hasPermission(Player player, String permission) {
        return PermissionApi.API.getPermission(player, permission).map(false);
    }

    public static int getMaxGuildMembers(Level level, UUID playerId) {
        return RoleApi.API.forceGetNonNullOption(level, playerId, AlliesOptions.SERIALIZER).maxGuildMembers();
    }

    public static int getMaxPartyMembers(Level level, UUID playerId) {
        return RoleApi.API.forceGetNonNullOption(level, playerId, AlliesOptions.SERIALIZER).maxPartyMembers();
    }
}
