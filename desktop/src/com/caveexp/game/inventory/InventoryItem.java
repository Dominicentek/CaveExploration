package com.caveexp.game.inventory;

import com.caveexp.game.item.Item;
import com.caveexp.game.item.Tool;
import com.caveexp.util.Registry;

public class InventoryItem {
    public String item;
    public int count;
    public InventoryItem(String item, int count) {
        this.item = item;
        this.count = count;
    }
    public InventoryItem(String item) {
        this(item, Registry.ITEMS.get(item) instanceof Tool ? ((Tool)Registry.ITEMS.get(item)).durability : 1);
    }
    public InventoryItem(Item item, int count) {
        this(Registry.ITEMS.getID(item), count);
    }
    public InventoryItem(Item item) {
        this(Registry.ITEMS.getID(item));
    }
    public InventoryItem() {
        this("air", 0);
    }
}
