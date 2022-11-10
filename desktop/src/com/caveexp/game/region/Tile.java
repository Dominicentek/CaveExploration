package com.caveexp.game.region;

import com.caveexp.game.ores.Ore;
import com.caveexp.util.Registry;

public class Tile {
    public boolean light = false;
    public boolean air = false;
    public Ore ore = Registry.ORES.get("stone");
    public TileItem item = TileItem.NOTHING;
}
