package earth.terrarium.argonauts.client.screens.base.members;

import com.teamresourceful.resourcefullib.client.components.selection.ListEntry;
import com.teamresourceful.resourcefullib.client.components.selection.SelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import org.jetbrains.annotations.Nullable;

public class MemberSettingList extends SelectionList<ListEntry> {

    private ListEntry selected;

    public MemberSettingList(int x, int y, int width, int height) {
        super(x, y, width, height, 20, entry -> {}, true);
    }

    @Override
    public void setSelected(@Nullable ListEntry entry) {
        super.setSelected(entry);
        this.selected = entry;
    }

    @Nullable
    @Override
    public GuiEventListener getFocused() {
        return selected != null ? selected : super.getFocused();
    }
}
