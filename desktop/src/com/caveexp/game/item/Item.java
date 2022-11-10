package com.caveexp.game.item;

import com.badlogic.gdx.graphics.Texture;
import com.caveexp.assets.Loader;
import com.caveexp.game.region.TileItem;
import com.caveexp.util.GraphicsUtils;
import com.caveexp.util.Registry;

public class Item {
    public String name;
    public Texture texture;
    public boolean hideFromSandboxInventory = false;
    public Item(String name, Texture texture) {
        this.name = name;
        this.texture = texture;
    }
    public static Texture getPickaxeTexture(int color) {
        return GraphicsUtils.assemble(Loader.get("stick", Loader.IMAGE), Loader.get("pickaxe", Loader.IMAGE), color);
    }
    public static Texture getSwordTexture(int color) {
        return GraphicsUtils.assemble(Loader.get("swordstick", Loader.IMAGE), Loader.get("sword", Loader.IMAGE), color);
    }
    public Item hideFromSandboxInventory() {
        return hideFromSandboxInventory(true);
    }
    public Item hideFromSandboxInventory(boolean hideFromSandboxInventory) {
        this.hideFromSandboxInventory = hideFromSandboxInventory;
        return this;
    }
    public static void register() {
        Registry.ITEMS.register("air", new Item("", Loader.get("blank", Loader.IMAGE)).hideFromSandboxInventory());
        Registry.ITEMS.register("bronze", new Item("Bronze", Loader.get("bronze", Loader.IMAGE)));
        Registry.ITEMS.register("iron", new Item("Iron", Loader.get("iron", Loader.IMAGE)));
        Registry.ITEMS.register("stick", new Item("Stick", Loader.get("stick", Loader.IMAGE)));
        Registry.ITEMS.register("coal", new Item("Coal", Loader.get("coal", Loader.IMAGE)));
        Registry.ITEMS.register("diamond", new Item("Diamond", Loader.get("diamond", Loader.IMAGE)));
        Registry.ITEMS.register("gold", new Item("Gold", Loader.get("gold", Loader.IMAGE)));
        Registry.ITEMS.register("molten_tin", new Item("Molten Tin", Loader.get("moltentin", Loader.IMAGE)));
        Registry.ITEMS.register("molten_copper", new Item("Molten Copper", Loader.get("moltencopper", Loader.IMAGE)));
        Registry.ITEMS.register("silver", new Item("Silver", Loader.get("silver", Loader.IMAGE)));
        Registry.ITEMS.register("platinum", new Item("Platinum", Loader.get("platinum", Loader.IMAGE)));
        Registry.ITEMS.register("raw_copper", new Item("Raw Copper", Loader.get("rawcopper", Loader.IMAGE)));
        Registry.ITEMS.register("raw_tin", new Item("Raw Tin", Loader.get("rawtin", Loader.IMAGE)));
        Registry.ITEMS.register("stone", new Item("Stone", Loader.get("stone", Loader.IMAGE)));
        Registry.ITEMS.register("torch", new PlaceItem("Torch", Loader.get("torch", Loader.IMAGE), Registry.SOUNDS.get("torch"), TileItem.TORCH));
        Registry.ITEMS.register("potion", new Item("Healing Potion", Loader.get("potion", Loader.IMAGE)));
        Registry.ITEMS.register("pickaxe_basic", new Pickaxe("Basic Pickaxe", getPickaxeTexture(0x7F7F7FFF), 50, 1));
        Registry.ITEMS.register("pickaxe_bronze", new Pickaxe("Bronze Pickaxe", getPickaxeTexture(0xBD7C32FF), 150, 2));
        Registry.ITEMS.register("pickaxe_iron", new Pickaxe("Iron Pickaxe", getPickaxeTexture(0xCBCDCDFF), 300, 3));
        Registry.ITEMS.register("pickaxe_gold", new Pickaxe("Gold Pickaxe", getPickaxeTexture(0xFFFF00FF), 350, 4));
        Registry.ITEMS.register("pickaxe_silver", new Pickaxe("Silver Pickaxe", getPickaxeTexture(0xFFFFFFFF), 500, 4));
        Registry.ITEMS.register("pickaxe_diamond", new Pickaxe("Diamond Pickaxe", getPickaxeTexture(0x00DAE5FF), 1000, 5));
        Registry.ITEMS.register("pickaxe_platinum", new Pickaxe("Platinum Pickaxe", getPickaxeTexture(0xDAE5CAFF), 2000, 6));
        Registry.ITEMS.register("pickaxe_dev", new Pickaxe("debug", getPickaxeTexture(0x000000FF), Integer.MAX_VALUE, -1).hideFromSandboxInventory());
        Registry.ITEMS.register("sword_basic", new Sword("Basic Sword", getSwordTexture(0x7F7F7FFF), 50, 2));
        Registry.ITEMS.register("sword_bronze", new Sword("Bronze Sword", getSwordTexture(0xBD7C32FF), 150, 3));
        Registry.ITEMS.register("sword_iron", new Sword("Iron Sword", getSwordTexture(0xCBCDCDFF), 300, 5));
        Registry.ITEMS.register("sword_gold", new Sword("Gold Sword", getSwordTexture(0xFFFF00FF), 350, 7));
        Registry.ITEMS.register("sword_silver", new Sword("Silver Sword", getSwordTexture(0xFFFFFFFF), 500, 7));
        Registry.ITEMS.register("sword_diamond", new Sword("Diamond Sword", getSwordTexture(0x00DAE5FF), 1000, 10));
        Registry.ITEMS.register("sword_platinum", new Sword("Platinum Sword", getSwordTexture(0xDAE5CAFF), 2000, 15));
        Registry.ITEMS.register("sword_dev", new Sword("debug", getSwordTexture(0x000000FF), Integer.MAX_VALUE, Integer.MAX_VALUE).hideFromSandboxInventory());
        Registry.ITEMS.register("pickaxe_croc", new Pickaxe("Croc Pickaxe", Loader.get("croc_pickaxe", Loader.IMAGE), 42069, 6).hideFromSandboxInventory());
    }
}
