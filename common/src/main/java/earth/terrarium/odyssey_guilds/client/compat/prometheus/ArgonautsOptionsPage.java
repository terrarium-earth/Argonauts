package earth.terrarium.odyssey_guilds.client.compat.prometheus;

import earth.terrarium.odyssey_guilds.common.compat.prometheus.ArgonautsOptions;
import earth.terrarium.odyssey_guilds.common.constants.ConstantComponents;
import earth.terrarium.olympus.client.components.Widgets;
import earth.terrarium.olympus.client.utils.State;
import earth.terrarium.prometheus.api.roles.client.Page;
import earth.terrarium.prometheus.client.utils.UiUtils;
import earth.terrarium.prometheus.common.handlers.role.Role;
import earth.terrarium.prometheus.common.menus.content.RoleEditContent;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.Layout;

public class ArgonautsOptionsPage implements Page {

    private final RoleEditContent content;

    private final State<Integer> maxClaimsBox;
    private final State<Integer> maxChunkLoaded;

    public ArgonautsOptionsPage(RoleEditContent content, Runnable ignored) {
        this.content = content;

        Role role = content.selected();
        ArgonautsOptions options = role.getNonNullOption(ArgonautsOptions.SERIALIZER);

        this.maxClaimsBox = State.of(options.maxGuildMembers());
        this.maxChunkLoaded = State.of(options.maxPartyMembers());
    }

    @Override
    public Layout getContents(int width, int height) {
        GridLayout layout = new GridLayout().rowSpacing(5);

        Role role = content.selected();
        ArgonautsOptions options = role.getNonNullOption(ArgonautsOptions.SERIALIZER);

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
        ArgonautsOptions options = role.getNonNullOption(ArgonautsOptions.SERIALIZER);
        ArgonautsOptions newOptions = new ArgonautsOptions(
            maxClaimsBox.get(),
            maxChunkLoaded.get()
        );
        if (!newOptions.equals(options)) {
            role.setData(newOptions);
        }
    }
}
