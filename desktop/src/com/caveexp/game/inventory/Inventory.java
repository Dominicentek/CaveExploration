package com.caveexp.game.inventory;

import com.caveexp.game.Game;
import com.caveexp.game.SoundEvent;
import com.caveexp.game.item.Item;
import com.caveexp.game.item.Tool;
import com.caveexp.game.recipes.Recipe;
import com.caveexp.util.Registry;

public class Inventory {
    public static final int SLOTS = 10;
    public InventoryItem[] items = new InventoryItem[SLOTS];
    public Inventory() {
        for (int i = 0; i < SLOTS; i++) {
            putItem(new InventoryItem(), i);
        }
        if (Game.sandboxMode) return;
        putItem(new InventoryItem("potion", 5), 9);
    }
    public void putItem(InventoryItem item, int slot) {
        items[slot] = item;
    }
    public void addItem(InventoryItem item) {
        if (cannotBeAdded(item) || item.item.equals("air")) return;
        int slot = getSlotFor(item);
        if (slot == -1) {
            for (int i = 0; i < SLOTS; i++) {
                if ((items[i].item.equals(item.item) || items[i].count == 0) && !(Registry.ITEMS.get(items[i].item) instanceof Tool)) {
                    slot = i;
                    break;
                }
            }
        }
        if (slot == -1) return;
        items[slot].item = item.item;
        items[slot].count += item.count;
    }
    public int getSlotFor(InventoryItem item) {
        for (int i = 0; i < SLOTS; i++) {
            if (items[i].item.equals(item.item) && !(Registry.ITEMS.get(items[i].item) instanceof Tool)) return i;
        }
        return -1;
    }
    public void removeItem(InventoryItem item) {
        if (!hasEnough(item)) return;
        int itemsRemaining = item.count;
        for (int i = 0; i < SLOTS; i++) {
            if (items[i].item.equals(item.item)) {
                if (Registry.ITEMS.get(item.item) instanceof Tool) {
                    itemsRemaining--;
                    items[i].item = "air";
                    items[i].count = 0;
                    Registry.SOUNDS.get("break").play();
                    if (itemsRemaining == 0) return;
                }
                else {
                    itemsRemaining -= items[i].count;
                    items[i].count = 0;
                    if (itemsRemaining < 0) {
                        items[i].count = -itemsRemaining;
                        break;
                    }
                    if (items[i].count == 0) {
                        items[i].item = "air";
                    }
                }
            }
        }
    }
    public void craft(Recipe recipe) {
        Game.grantAchievement(Registry.ACHIEVEMENTS.get("craft_first_item"));
        if (!recipe.canCraft(this)) return;
        removeItem(recipe.item1);
        removeItem(recipe.item2);
        addItem(recipe.result);
    }
    public boolean hasEnough(InventoryItem item) {
        int itemsRemaining = item.count;
        for (int i = 0; i < SLOTS; i++) {
            if (items[i].item.equals(item.item)) {
                if (Registry.ITEMS.get(item.item) instanceof Tool) itemsRemaining--;
                else itemsRemaining -= items[i].count;
            }
        }
        return itemsRemaining <= 0;
    }
    public boolean cannotBeAdded(InventoryItem item) {
        for (int i = 0; i < SLOTS; i++) {
            if ((items[i].item.equals(item.item) || items[i].count == 0) && !(Registry.ITEMS.get(items[i].item) instanceof Tool)) return false;
        }
        return true;
    }
}
