package earth.terrarium.odyssey_guilds.client.compat.prometheus;

import earth.terrarium.odyssey_guilds.common.compat.roles.OdysseyGuildsOptions;
import earth.terrarium.odyssey_guilds.common.constants.ConstantComponents;
import earth.terrarium.olympus.client.components.Widgets;
import earth.terrarium.olympus.client.utils.State;
import earth.terrarium.prometheus.api.roles.client.Page;
import earth.terrarium.prometheus.client.utils.UiUtils;
import earth.terrarium.prometheus.common.handlers.role.Role;
import earth.terrarium.prometheus.common.menus.content.RoleEditContent;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.Layout;

public class OdysseyGuildsOptionsPage implements Page {

    private final RoleEditContent content;

    private final State<Integer> maxClaimsBox;
    private final State<Integer> maxChunkLoaded;

    public OdysseyGuildsOptionsPage(RoleEditContent content, Runnable ignored) {
        this.content = content;

        Role role = content.selected();
        OdysseyGuildsOptions options = role.getNonNullOption(OdysseyGuildsOptions.SERIALIZER);

        this.maxClaimsBox = State.of(options.maxGuildMembers());
        this.maxChunkLoaded = State.of(options.maxPartyMembers());
    }

    @Override
    public Layout getContents(int width, int height) {
        GridLayout layout = new GridLayout().rowSpacing(5);

        Role role = content.selected();
        OdysseyGuildsOptions options = role.getNonNullOption(OdysseyGuildsOptions.SERIALIZER);

        UiUtils.addLine(
            layout, 0, width,
            ConstantComponents.MAX_GUILD_MEMBERS,
            (w) -> Widgets.intInput(maxClaimsBox, textBox -> textBox.withSize(w, 20))
        );

        UiUtils.addLine(
            layout, 1, width,
            ConstantComponents.MAX_PARTY_MEMBERS,
            (w) -> Widgets.intInput(maxChunkLoaded, textBox -> textBox.withSize(w, 20))
        );

        return layout;
    }

    @Override
    public void save(Role role) {
        OdysseyGuildsOptions options = role.getNonNullOption(OdysseyGuildsOptions.SERIALIZER);
        OdysseyGuildsOptions newOptions = new OdysseyGuildsOptions(
            maxClaimsBox.get(),
            maxChunkLoaded.get()
        );
        if (!newOptions.equals(options)) {
            role.setData(newOptions);
        }
    }
}
