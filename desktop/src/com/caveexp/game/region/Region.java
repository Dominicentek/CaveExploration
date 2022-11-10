package com.caveexp.game.region;

import java.util.ArrayList;

public class Region {
    public static final int WIDTH = 240;
    public static final int HEIGHT = 240;
    public static final int REGIONS = 5;
    public static final Region[] regions = new Region[REGIONS];
    public Tile[][] tiles = new Tile[WIDTH][HEIGHT];
    public boolean noItems = false;
    public static final int[] LIGHTMAP = {
        0,0,1,1,1,0,0,
        0,1,1,1,1,1,0,
        1,1,1,1,1,1,1,
        1,1,1,1,1,1,1,
        1,1,1,1,1,1,1,
        0,1,1,1,1,1,0,
        0,0,1,1,1,0,0
    };
    public double playerX = 0;
    public double playerY = 0;
    public int spiderKillRequirement = 0;
    public ArrayList<SpiderFight> activeFights = new ArrayList<>();
    public int id;
    public Region(int id) {
        this.id = id;
    }
    public void updateLight() {
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                tiles[x][y].light = false;
            }
        }
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (tiles[x][y].item == TileItem.TORCH) {
                    for (int X = -3; X <= 3; X++) {
                        for (int Y = -3; Y <= 3; Y++) {
                            int index = ((X + 3) * 7) + (Y + 3);
                            if (LIGHTMAP[index] == 0) continue;
                            if (x + X < 0 || y + Y < 0 || x + X >= WIDTH || y + Y >= HEIGHT) continue;
                            tiles[x + X][y + Y].light = true;
                        }
                    }
                }
            }
        }
    }
}
