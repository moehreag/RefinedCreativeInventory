package io.github.darkkronicle.refinedcreativeinventory.tabs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;

public class TabHolder {

    private final static TabHolder INSTANCE = new TabHolder();

    public static TabHolder getInstance() {
        return INSTANCE;
    }

    @Getter @Setter private List<ItemTab> tabs = new ArrayList<>();

    private TabHolder() {}

    public void addTab(ItemTab tab) {
        this.tabs.add(tab);
        Collections.sort(tabs);
    }

    public void setVanilla() {
        int i = 0;
        for (ItemGroup group : ItemGroups.getGroupsToDisplay()) {
            i++;
            if (group.equals(Registries.ITEM_GROUP.get(ItemGroups.HOTBAR)) || group.equals(Registries.ITEM_GROUP.get(ItemGroups.SEARCH)) || group.equals(Registries.ITEM_GROUP.get(ItemGroups.INVENTORY))) {
                continue;
            }
            addTab(CustomTab.fromGroup(group, i));
        }
        addTab(new InventoryTab());
        addTab(new HotbarTab());
        Collections.sort(tabs);
    }
}
