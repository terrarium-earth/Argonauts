package earth.terrarium.odyssey_allies.client.compat.prometheus;

import earth.terrarium.odyssey_allies.common.compat.roles.AlliesOptions;
import earth.terrarium.odyssey_allies.common.compat.roles.AlliesPermissions;
import earth.terrarium.prometheus.api.permissions.PermissionApi;
import earth.terrarium.prometheus.api.roles.client.PageApi;

public class PrometheusClientCompat {

    public static void init() {
        PageApi.API.register(AlliesOptions.SERIALIZER.id(), AlliesOptionsPage::new);

        PermissionApi.API.addAutoComplete(AlliesPermissions.TELEPORT);
        PermissionApi.API.addAutoComplete(AlliesPermissions.CREATE_PARTY);
        PermissionApi.API.addAutoComplete(AlliesPermissions.CREATE_GUILD);
    }
}
