package com.caveexp.game.item;

import com.badlogic.gdx.graphics.Texture;
import com.caveexp.game.ores.Ore;

public class OreItem extends Item {
    public final Ore ore;
    public OreItem(String name, Texture texture, Ore ore) {
        super(name, texture);
        this.ore = ore;
    }
}
