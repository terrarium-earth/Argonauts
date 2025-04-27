package earth.terrarium.odyssey_guilds.client.compat.prometheus;

import earth.terrarium.odyssey_guilds.common.compat.roles.OdysseyGuildsOptions;
import earth.terrarium.odyssey_guilds.common.compat.roles.OdysseyGuildsPermissions;
import earth.terrarium.prometheus.api.permissions.PermissionApi;
import earth.terrarium.prometheus.api.roles.client.PageApi;

public class PrometheusClientCompat {

    public static void init() {
        PageApi.API.register(OdysseyGuildsOptions.SERIALIZER.id(), OdysseyGuildsOptionsPage::new);

        PermissionApi.API.addAutoComplete(OdysseyGuildsPermissions.TELEPORT);
        PermissionApi.API.addAutoComplete(OdysseyGuildsPermissions.CREATE_PARTY);
        PermissionApi.API.addAutoComplete(OdysseyGuildsPermissions.CREATE_GUILD);
    }
}
