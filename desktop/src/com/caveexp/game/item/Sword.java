package com.caveexp.game.item;

import com.badlogic.gdx.graphics.Texture;

public class Sword extends Tool {
    public int damage;
    public Sword(String name, Texture texture, int durability, int damage) {
        super(name, texture, durability);
        this.damage = damage;
    }
}
