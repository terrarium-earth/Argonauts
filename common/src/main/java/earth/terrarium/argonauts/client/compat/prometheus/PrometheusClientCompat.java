package earth.terrarium.argonauts.client.compat.prometheus;

import earth.terrarium.argonauts.common.compat.roles.ArgonautsOptions;
import earth.terrarium.argonauts.common.compat.roles.ArgonuatsPermissions;
import earth.terrarium.prometheus.api.permissions.PermissionApi;
import earth.terrarium.prometheus.api.roles.client.PageApi;

public class PrometheusClientCompat {

    public static void init() {
        PageApi.API.register(ArgonautsOptions.SERIALIZER.id(), ArgonautsOptionsPage::new);

        PermissionApi.API.addAutoComplete(ArgonuatsPermissions.TELEPORT);
        PermissionApi.API.addAutoComplete(ArgonuatsPermissions.CREATE_PARTY);
        PermissionApi.API.addAutoComplete(ArgonuatsPermissions.CREATE_GUILD);
    }
}
