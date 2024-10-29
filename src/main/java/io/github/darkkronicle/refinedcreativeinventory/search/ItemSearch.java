package io.github.darkkronicle.refinedcreativeinventory.search;

import java.util.List;

import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;

public interface ItemSearch {
    List<InventoryItem> search(List<InventoryItem> items);
}
