package io.github.darkkronicle.refinedcreativeinventory.items;

import java.util.*;

import io.github.darkkronicle.refinedcreativeinventory.mixin.ItemGroupAccessor;
import io.github.darkkronicle.refinedcreativeinventory.tabs.ItemTab;
import io.github.darkkronicle.refinedcreativeinventory.util.ItemSerializer;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.BlockStateComponent;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;

public class ItemHolder {

	private static final ItemHolder INSTANCE = new ItemHolder();

	public static ItemHolder getInstance() {
		return INSTANCE;
	}

	@Getter
	private List<InventoryItem> allItems = new ArrayList<>();

	@Getter
	private List<ItemTab> tabs = new ArrayList<>();

	public void setDefaults() {
		for (Item item : Registries.ITEM) {
			getOrCreate(new ItemStack(item));
		}
		for (int i = 0; i < 15; i++) {
			ItemStack stack = new ItemStack(Items.LIGHT);
			stack.set(DataComponentTypes.BLOCK_STATE, BlockStateComponent.DEFAULT.with(Properties.LEVEL_15, i));
			stack.set(DataComponentTypes.CUSTOM_NAME, Text.literal("§dLight " + i));
			getOrCreate(stack).addFlag(new ItemFlag("custom_light", i * 10));
		}
		getOrCreate(new ItemStack(Items.LIGHT)).addFlag(new ItemFlag("custom_light", 150));
//        Collections.sort(allItems);
	}

	public void populateGroups() {
		for (ItemGroup group : Registries.ITEM_GROUP) {
			if (group.equals(Registries.ITEM_GROUP.get(ItemGroups.HOTBAR)) || group.equals(Registries.ITEM_GROUP.get(ItemGroups.SEARCH))) {
				continue;
			}
			ItemGroup.Entries entries = (stack, visibility) -> addGroup(stack, group);
			((ItemGroupAccessor) group).getEntryCollector()
					.accept(new ItemGroup.DisplayContext(FeatureSet.empty(), true, MinecraftClient.getInstance().world.getRegistryManager()), entries);
		}
	}

	public InventoryItem getOrCreate(ItemStack stack) {
		return allItems.stream().filter(item -> ItemSerializer.areEqual(item.getStack(), stack)).findFirst().orElseGet(() -> {
			BasicInventoryItem item = new BasicInventoryItem(stack);
			allItems.add(item);
			return item;
		});
	}

	public void addIfNotExist(InventoryItem item) {
		if (!allItems.contains(item)) {
			allItems.add(item);
			Collections.sort(allItems);
		}
	}

	public boolean contains(InventoryItem item) {
		return allItems.contains(item);
	}

	public Optional<InventoryItem> get(ItemStack stack) {
		return allItems.stream().filter(item -> ItemSerializer.areEqual(item.getStack(), stack)).findFirst();
	}

	public void setItems(List<InventoryItem> items) {
		Collections.sort(items);
		this.allItems = items;
	}

	public Map<ItemGroup, List<InventoryItem>> getGroups() {
		populateGroups();
		HashMap<ItemGroup, List<InventoryItem>> stacks = new HashMap<>();
		for (InventoryItem item : ItemHolder.getInstance().getAllItems()) {
			if (item.getGroups().isEmpty()) {
				stacks.compute(null, (k, v) -> {
					if (v == null) {
						return new ArrayList<>(List.of(item));
					}
					v.add(item);
					return v;
				});
				continue;
			}
			for (ItemGroup group : item.getGroups()) {
				stacks.compute(group, (k, v) -> {
					if (v == null) {
						return new ArrayList<>(List.of(item));
					}
					v.add(item);
					return v;
				});
			}
		}
		return stacks;
	}

	public void addGroup(ItemStack item, ItemGroup group) {
		getOrCreate(item).addGroup(group);
	}

}
