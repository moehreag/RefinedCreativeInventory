package io.github.darkkronicle.refinedcreativeinventory.items;

import java.util.*;
import java.util.stream.Stream;

import io.github.darkkronicle.refinedcreativeinventory.mixin.ItemGroupAccessor;
import io.github.darkkronicle.refinedcreativeinventory.tabs.ItemTab;
import io.github.darkkronicle.refinedcreativeinventory.util.ItemSerializer;
import lombok.Getter;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;

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
			/*List<ItemGroup> groups = Registries.ITEM_GROUP.stream()
					.filter(group -> group.contains(item.getDefaultStack()))
					.toList();
			if (groups.isEmpty()) {
				getOrCreate(new ItemStack(item));
			} else {
				ItemStack stack = item.getDefaultStack();
				InventoryItem i = getOrCreate(stack);
				for (ItemGroup group : groups) {
					i.addGroup(group);
				}
				//DefaultedList<ItemStack> stackList = DefaultedList.of();
				//item.appendStacks(item.getGroup(), stackList);
				//for (ItemStack stack : stackList) {
				//	InventoryItem i = getOrCreate(stack);
				//}
			}*/
		}
		for (int i = 0; i < 15; i++) {
			ItemStack stack = new ItemStack(Items.LIGHT);
			NbtCompound nbt = new NbtCompound();
			NbtCompound blockState = new NbtCompound();
			blockState.putString("level", String.valueOf(i));
			nbt.put("BlockStateTag", blockState);
			stack.setNbt(nbt);
			stack.setCustomName(Text.literal("§dLight " + i));
			getOrCreate(stack).addFlag(new ItemFlag("custom_light", i * 10));
		}
		getOrCreate(new ItemStack(Items.LIGHT)).addFlag(new ItemFlag("custom_light", 150));
		populateGroups();
//        Collections.sort(allItems);
	}

	public void populateGroups() {
		for (ItemGroup group : Registries.ITEM_GROUP) {
			if (group.equals(Registries.ITEM_GROUP.get(ItemGroups.HOTBAR)) || group.equals(Registries.ITEM_GROUP.get(ItemGroups.SEARCH))) {
				continue;
			}
			ItemGroup.Entries entries = (stack, visibility) -> addGroup(stack, group);
			((ItemGroupAccessor)group).getEntryCollector()
					.accept(new ItemGroup.DisplayContext(FeatureSet.empty(), true, RegistryWrapper.WrapperLookup.of(Stream.empty())), entries);

			/*for (ItemStack item : group.getDisplayStacks()) {
				addGroup(item, group);
			}*/
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
