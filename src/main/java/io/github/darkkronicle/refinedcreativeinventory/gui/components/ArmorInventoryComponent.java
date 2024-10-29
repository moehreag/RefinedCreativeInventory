package io.github.darkkronicle.refinedcreativeinventory.gui.components;

import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Equipment;
import net.minecraft.util.Identifier;

public class ArmorInventoryComponent extends IconInventoryComponent {

	public ArmorInventoryComponent(InventoryScreen parent, EquipmentSlot slot, Identifier icon, int index) {
		super(parent, icon, index);
		setAllowed(stack -> {
			Equipment equipment = Equipment.fromStack(stack);
			return equipment != null && slot == equipment.getSlotType();
		});
	}

}
