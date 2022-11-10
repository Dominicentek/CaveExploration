package com.caveexp.game.recipes;

import com.caveexp.game.inventory.Inventory;
import com.caveexp.game.inventory.InventoryItem;
import com.caveexp.util.Registry;
import java.util.ArrayList;

public class Recipe {
    public InventoryItem item1;
    public InventoryItem item2;
    public InventoryItem result;
    public Recipe(InventoryItem item1, InventoryItem item2, InventoryItem result) {
        this.item1 = item1;
        this.item2 = item2;
        this.result = result;
    }
    public boolean canCraft(Inventory inventory) {
        return inventory.hasEnough(item1) && inventory.hasEnough(item2);
    }
    public static void register() {
        Registry.RECIPES.register("bronze", new Recipe(new InventoryItem("molten_copper"), new InventoryItem("molten_tin"), new InventoryItem("bronze")));
        Registry.RECIPES.register("molten_copper", new Recipe(new InventoryItem("raw_copper"), new InventoryItem("coal"), new InventoryItem("molten_copper")));
        Registry.RECIPES.register("molten_tin", new Recipe(new InventoryItem("raw_tin"), new InventoryItem("coal"), new InventoryItem("molten_tin")));
        Registry.RECIPES.register("torch", new Recipe(new InventoryItem("coal"), new InventoryItem("stick"), new InventoryItem("torch", 4)));
        Registry.RECIPES.register("stick", new Recipe(new InventoryItem("stone", 3), new InventoryItem("stick"), new InventoryItem("pickaxe_basic")));
        Registry.RECIPES.register("stick", new Recipe(new InventoryItem("bronze", 3), new InventoryItem("stick"), new InventoryItem("pickaxe_bronze")));
        Registry.RECIPES.register("stick", new Recipe(new InventoryItem("diamond", 3), new InventoryItem("stick"), new InventoryItem("pickaxe_diamond")));
        Registry.RECIPES.register("stick", new Recipe(new InventoryItem("gold", 3), new InventoryItem("stick"), new InventoryItem("pickaxe_gold")));
        Registry.RECIPES.register("stick", new Recipe(new InventoryItem("iron", 3), new InventoryItem("stick"), new InventoryItem("pickaxe_iron")));
        Registry.RECIPES.register("stick", new Recipe(new InventoryItem("platinum", 3), new InventoryItem("stick"), new InventoryItem("pickaxe_platinum")));
        Registry.RECIPES.register("stick", new Recipe(new InventoryItem("silver", 3), new InventoryItem("stick"), new InventoryItem("pickaxe_silver")));
        Registry.RECIPES.register("stick", new Recipe(new InventoryItem("stone", 2), new InventoryItem("stick"), new InventoryItem("sword_basic")));
        Registry.RECIPES.register("stick", new Recipe(new InventoryItem("bronze", 2), new InventoryItem("stick"), new InventoryItem("sword_bronze")));
        Registry.RECIPES.register("stick", new Recipe(new InventoryItem("diamond", 2), new InventoryItem("stick"), new InventoryItem("sword_diamond")));
        Registry.RECIPES.register("stick", new Recipe(new InventoryItem("gold", 2), new InventoryItem("stick"), new InventoryItem("sword_gold")));
        Registry.RECIPES.register("stick", new Recipe(new InventoryItem("iron", 2), new InventoryItem("stick"), new InventoryItem("sword_iron")));
        Registry.RECIPES.register("stick", new Recipe(new InventoryItem("platinum", 2), new InventoryItem("stick"), new InventoryItem("sword_platinum")));
        Registry.RECIPES.register("stick", new Recipe(new InventoryItem("silver", 2), new InventoryItem("stick"), new InventoryItem("sword_silver")));
    }
}
