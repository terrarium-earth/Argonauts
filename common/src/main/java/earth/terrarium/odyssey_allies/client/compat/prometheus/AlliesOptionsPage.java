package earth.terrarium.odyssey_allies.client.compat.prometheus;

import earth.terrarium.odyssey_allies.common.compat.roles.AlliesOptions;
import earth.terrarium.odyssey_allies.common.constants.ConstantComponents;
import earth.terrarium.olympus.client.components.Widgets;
import earth.terrarium.olympus.client.utils.State;
import earth.terrarium.prometheus.api.roles.client.Page;
import earth.terrarium.prometheus.client.utils.UiUtils;
import earth.terrarium.prometheus.common.handlers.role.Role;
import earth.terrarium.prometheus.common.menus.content.RoleEditContent;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.Layout;

public class AlliesOptionsPage implements Page {

    private final RoleEditContent content;

    private final State<Integer> maxClaimsBox;
    private final State<Integer> maxChunkLoaded;

    public AlliesOptionsPage(RoleEditContent content, Runnable ignored) {
        this.content = content;

        Role role = content.selected();
        AlliesOptions options = role.getNonNullOption(AlliesOptions.SERIALIZER);

        this.maxClaimsBox = State.of(options.maxGuildMembers());
        this.maxChunkLoaded = State.of(options.maxPartyMembers());
    }

    @Override
    public Layout getContents(int width, int height) {
        GridLayout layout = new GridLayout().rowSpacing(5);

        Role role = content.selected();
        AlliesOptions options = role.getNonNullOption(AlliesOptions.SERIALIZER);

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
        AlliesOptions options = role.getNonNullOption(AlliesOptions.SERIALIZER);
        AlliesOptions newOptions = new AlliesOptions(
            maxClaimsBox.get(),
            maxChunkLoaded.get()
        );
        if (!newOptions.equals(options)) {
            role.setData(newOptions);
        }
    }
}
