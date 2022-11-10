package com.caveexp.game.item;

import com.badlogic.gdx.graphics.Texture;
import com.caveexp.game.Game;
import com.caveexp.game.SoundEvent;
import com.caveexp.game.inventory.InventoryItem;
import com.caveexp.util.Registry;

public abstract class Tool extends Item {
    public int durability;
    public Tool(String name, Texture texture, int durability) {
        super(name, texture);
        this.durability = durability;
    }
    public void damage() {
        Game.inventory.items[Game.selectedSlot].count--;
        if (Game.inventory.items[Game.selectedSlot].count == 0) {
            Registry.SOUNDS.get("break").play();
            Game.inventory.items[Game.selectedSlot].item = "air";
        }
    }
}
