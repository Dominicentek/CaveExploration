package com.caveexp.game.ores;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.caveexp.assets.Loader;
import com.caveexp.game.item.OreItem;
import com.caveexp.util.GraphicsUtils;
import com.caveexp.util.Registry;
import java.util.ArrayList;
import java.util.HashMap;

public class Ore {
    public String name;
    public int spawnChance;
    public String drop;
    public int tier;
    public int region;
    public Texture texture;
    public OreItem item;
    public int color;
    public Ore(String name, int color, int spawnChance, String drop, int tier, int region, boolean solidColor) {
        this.name = name;
        this.spawnChance = spawnChance;
        this.drop = drop;
        this.tier = tier;
        this.region = region;
        this.color = color;
        if (solidColor) {
            Pixmap pixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
            pixmap.setColor(color);
            pixmap.fillRectangle(0, 0, 32, 32);
            texture = new Texture(pixmap);
        }
        else texture = GraphicsUtils.assemble(Loader.get("ore", Loader.IMAGE), Loader.get("oreoverlay", Loader.IMAGE), color);
    }
    public Ore registerItem() {
        item = (OreItem)Registry.ITEMS.register("ore_" + name.toLowerCase(), new OreItem(name, texture, this));
        return this;
    }
    public static void register() {
        Registry.ORES.register("coal", new Ore("Coal", 0x000000FF, 15, "coal", 1, 0, false).registerItem());
        Registry.ORES.register("copper", new Ore("Copper", 0xC87533FF, 13, "raw_copper", 1, 0, false).registerItem());
        Registry.ORES.register("diamond", new Ore("Diamond", 0x00DAE5FF, 2, "diamond", 4, 2, false).registerItem());
        Registry.ORES.register("gold", new Ore("Gold", 0xFFFF00FF, 7, "gold", 3, 1, false).registerItem());
        Registry.ORES.register("iron", new Ore("Iron", 0xAA5546FF, 10, "iron", 2, 1, false).registerItem());
        Registry.ORES.register("stone", new Ore("Stone", 0x7F7F7F00, 0, "stone", 0, 0, false).registerItem());
        Registry.ORES.register("platinum", new Ore("Platinum", 0xDAE5CAFF, 1, "platinum", 5, 3, false).registerItem());
        Registry.ORES.register("silver", new Ore("Silver", 0xFFFFFFFF, 4, "silver", 2, 2, false).registerItem());
        Registry.ORES.register("tin", new Ore("Tin", 0xB4B4B4FF, 13, "raw_tin", 1, 0, false).registerItem());
        Registry.ORES.register("gray", new Ore("", 0x7F7F7FFF, 0, "air", 6, 0, true));
        Registry.ORES.register("black", new Ore("", 0x000000FF, 0, "air", 7, 0, true));
    }
}
