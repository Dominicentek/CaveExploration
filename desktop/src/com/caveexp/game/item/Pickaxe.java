package com.caveexp.game.item;

import com.badlogic.gdx.graphics.Texture;

public class Pickaxe extends Tool {
    public int tier;
    public Pickaxe(String name, Texture texture, int durability, int tier) {
        super(name, texture, durability);
        this.tier = tier;
    }
}
