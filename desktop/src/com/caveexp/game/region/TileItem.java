package com.caveexp.game.region;

import com.caveexp.game.inventory.Inventory;
import com.caveexp.game.inventory.InventoryItem;
import com.caveexp.game.item.PlaceItem;
import com.caveexp.util.Registry;

public enum TileItem {
    NOTHING(null, 0x00000000),
    SPIDER(null, 0x000000FF),
    TORCH(null, 0xB57A29FF),
    LAVA(null, 0xF88007FF),
    STICK(new InventoryItem("stick"), 0xB57A29FF),
    POTION(new InventoryItem("potion"), 0xFF0000FF);
    private final InventoryItem pickupItem;
    public final int color;
    public PlaceItem placeItem = null;
    TileItem(InventoryItem pickupItem, int color) {
        this.pickupItem = pickupItem;
        this.color = color;
    }
    public boolean pickup(Inventory inventory) {
        if (pickupItem == null) return false;
        if (inventory.cannotBeAdded(pickupItem)) return false;
        Registry.SOUNDS.get("pickup").play();
        inventory.addItem(pickupItem);
        return true;
    }
}
