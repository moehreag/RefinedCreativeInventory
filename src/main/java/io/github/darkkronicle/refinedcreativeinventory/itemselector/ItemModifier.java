package io.github.darkkronicle.refinedcreativeinventory.itemselector;

import java.util.function.Consumer;

import io.github.darkkronicle.darkkore.gui.components.Component;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;

public interface ItemModifier {

    Component getComponent(Screen parent, Consumer<ItemStack> onClick, ItemStack base);

    ItemStack accept(ItemStack stack);

    boolean isActive();

    void setActive(boolean active);

}
