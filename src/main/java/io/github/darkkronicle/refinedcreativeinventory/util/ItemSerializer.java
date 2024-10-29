package io.github.darkkronicle.refinedcreativeinventory.util;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.darkkronicle.darkkore.DarkKore;
import io.github.darkkronicle.darkkore.config.impl.ConfigObject;
import lombok.experimental.UtilityClass;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

@UtilityClass
public class ItemSerializer {

	public ItemStack deserialize(ConfigObject config) {
		String nbt = config.get("nbt");
		if (nbt.charAt(0) != '{') {
			try {
				return new ItemStack(Registries.ITEM.get(Identifier.splitOn(nbt, ':')));
			} catch (Exception e) {
				DarkKore.LOGGER.error("Data " + nbt + " was invalid");
				return new ItemStack(Items.STONE);
			}
		}
		StringNbtReader reader = new StringNbtReader(new StringReader(nbt));
		try {
			return ItemStack.fromNbtOrEmpty(BuiltinRegistries.createWrapperLookup(), reader.parseCompound());
		} catch (CommandSyntaxException e) {
			return new ItemStack(Items.STONE);
		}
	}

	public void serialize(ConfigObject config, ItemStack stack) {
		if (areEqual(stack, new ItemStack(stack.getItem()))) {
			config.set("nbt", Registries.ITEM.getId(stack.getItem()).toString());
		} else {
			NbtCompound compound = new NbtCompound();
			stack.encode(BuiltinRegistries.createWrapperLookup());
			config.set("nbt", compound.toString());
		}
	}

	public boolean areEqual(ItemStack one, ItemStack two) {
		if (one.getItem().equals(Items.AIR)) {
			return two.getItem().equals(Items.AIR);
		}
		if (!one.isOf(two.getItem())) {
			return false;
		}
		if (one.getCount() != two.getCount()) {
			return false;
		}
		return ItemStack.areEqual(one, two);
	}

}
