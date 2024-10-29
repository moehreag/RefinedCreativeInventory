package io.github.darkkronicle.refinedcreativeinventory.items;

import java.util.*;

import com.mojang.datafixers.util.Pair;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class TagHolder {

    private final static TagHolder INSTANCE = new TagHolder();

    private Map<Identifier, List<Identifier>> tags = new HashMap<>();

    public static TagHolder getInstance() {
        return INSTANCE;
    }

    private TagHolder() {}

    public void populateTags() {
        tags.clear();
        for (Pair<TagKey<Item>, RegistryEntryList.Named<Item>> pair : Registries.ITEM.streamTagsAndEntries().toList()) {
            TagKey<Item> tagKey = pair.getFirst();
            RegistryEntryList.Named<Item> list = pair.getSecond();
            for (RegistryEntry<Item> item : list) {
                Identifier id = Registries.ITEM.getId(item.value());
                tags.compute(id, (k, v) -> {
                    if (v == null) {
                        return new ArrayList<>(List.of(tagKey.id()));
                    } else {
                        if (!v.contains(tagKey.id())) {
                            v.add(tagKey.id());
                        }
                        return v;
                    }
                });
            }
        }
    }

    public List<Identifier> getTags(Item item) {
        return Optional.ofNullable(tags.get(Registries.ITEM.getId(item))).orElseGet(ArrayList::new);
    }

}
