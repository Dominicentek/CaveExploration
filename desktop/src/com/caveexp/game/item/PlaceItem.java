package com.caveexp.game.item;

import com.badlogic.gdx.graphics.Texture;
import com.caveexp.game.SoundEvent;
import com.caveexp.game.region.TileItem;

public class PlaceItem extends Item {
    public final SoundEvent sound;
    public final TileItem item;
    public PlaceItem(String name, Texture texture, SoundEvent sound, TileItem item) {
        super(name, texture);
        this.sound = sound;
        this.item = item;
        item.placeItem = this;
    }
}
