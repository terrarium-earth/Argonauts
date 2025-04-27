package earth.terrarium.argonauts.client.compat.prometheus;

import earth.terrarium.argonauts.common.compat.prometheus.ArgonautsOptions;
import earth.terrarium.argonauts.common.compat.prometheus.ArgonautsPermissions;
import earth.terrarium.prometheus.api.permissions.PermissionApi;
import earth.terrarium.prometheus.api.roles.client.PageApi;

public class PrometheusClientCompat {

    public static void init() {
        PageApi.API.register(ArgonautsOptions.SERIALIZER.id(), ArgonautsOptionsPage::new);

        PermissionApi.API.addAutoComplete(ArgonautsPermissions.TELEPORT);
        PermissionApi.API.addAutoComplete(ArgonautsPermissions.CREATE_PARTY);
        PermissionApi.API.addAutoComplete(ArgonautsPermissions.CREATE_GUILD);
    }
}
