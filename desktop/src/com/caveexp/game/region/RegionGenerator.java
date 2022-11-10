package com.caveexp.game.region;

import com.caveexp.game.ores.Ore;
import com.caveexp.util.RNG;
import com.caveexp.util.Registry;
import java.awt.Point;
import java.util.ArrayList;

public class RegionGenerator {
    private static final Ore[] ores = Registry.ORES.array();
    public static long seed;
    public static void generateRegions(long seed) {
        RegionGenerator.seed = seed;
        RNG.setSeed(seed);
        for (int i = 0; i < Region.REGIONS; i++) {
            Region.regions[i] = generateRegion(i);
        }
    }
    public static Region generateRegion(int regionID) {
        boolean bossRoom = regionID == Region.REGIONS - 1;
        if (bossRoom) return createBossRoom();
        Region region = new Region(regionID);
        if (regionID == 0) region.spiderKillRequirement = 0;
        if (regionID == 1) region.spiderKillRequirement = 5;
        if (regionID == 2) region.spiderKillRequirement = 10;
        if (regionID == 3) region.spiderKillRequirement = 25;
        for (int x = 0; x < Region.WIDTH; x++) {
            for (int y = 0; y < Region.HEIGHT; y++) {
                Tile tile = new Tile();
                tile.ore = Registry.ORES.get("stone");
                tile.air = false;
                tile.light = false;
                region.tiles[x][y] = tile;
            }
        }
        for (int x = 0; x < Region.WIDTH / 6; x++) {
            for (int y = 0; y < Region.HEIGHT / 6; y++) {
                if (RNG.chance(3)) {
                    region.tiles[(x * 6) + 2][(y * 6) + 2].air = true;
                    region.tiles[(x * 6) + 3][(y * 6) + 2].air = true;
                    region.tiles[(x * 6) + 2][(y * 6) + 3].air = true;
                    region.tiles[(x * 6) + 3][(y * 6) + 3].air = true;
                }
            }
        }
        for (int x = 0; x < Region.WIDTH / 6; x++) {
            for (int y = 0; y < Region.HEIGHT / 6; y++) {
                if (!region.tiles[x * 6 + 2][y * 6 + 2].air) continue;
                if (x != 0) {
                    if (region.tiles[(x - 1) * 6 + 2][y * 6 + 2].air) {
                        for (int X = 4; X < 8; X++) {
                            for (int Y = 2; Y <= 3; Y++) {
                                region.tiles[(x - 1) * 6 + X][y * 6 + Y].air = true;
                            }
                        }
                    }
                }
                if (y != 0) {
                    if (region.tiles[x * 6 + 2][(y - 1) * 6 + 2].air) {
                        for (int Y = 4; Y < 8; Y++) {
                            for (int X = 2; X <= 3; X++) {
                                region.tiles[x * 6 + X][(y - 1) * 6 + Y].air = true;
                            }
                        }
                    }
                }
            }
        }
        ArrayList<Point> availableCornerPoints = new ArrayList<>();
        for (int x = 0; x < Region.WIDTH; x++) {
            for (int y = 0; y < Region.HEIGHT; y++) {
                boolean free = false;
                if (x != 0) if (region.tiles[x - 1][y].air) free = true;
                if (y != 0) if (region.tiles[x][y - 1].air) free = true;
                if (x != Region.WIDTH - 1) if (region.tiles[x + 1][y].air) free = true;
                if (y != Region.HEIGHT - 1) if (region.tiles[x][y + 1].air) free = true;
                if (free) availableCornerPoints.add(new Point(x, y));
            }
        }
        for (Point point : availableCornerPoints) {
            if (RNG.chance(2)) region.tiles[point.x][point.y].air = true;
        }
        for (int x = 0; x < Region.WIDTH; x++) {
            for (int y = 0; y < Region.HEIGHT; y++) {
                region.tiles[x][y].ore = randomOre(regionID);
            }
        }
        if (regionID == 1) {
            for (int x = 0; x < Region.WIDTH; x++) {
                for (int y = 0; y < Region.HEIGHT; y++) {
                    if (RNG.chance(250)) {
                        for (int X = x - 2; X <= x + 2; X++) {
                            for (int Y = y - 2; Y <= y + 2; Y++) {
                                if ((X == x - 2 && (Y == y - 2 || Y == y + 2)) || (X == x + 2 && (Y == y - 2 || Y == y + 2))) continue;
                                if (X < 0 || Y < 0 || X >= Region.WIDTH || Y >= Region.HEIGHT) continue;
                                region.tiles[X][Y].item = TileItem.LAVA;
                            }
                        }
                    }
                }
            }
        }
        ArrayList<Point> spawnPoints = new ArrayList<>();
        for (int x = 0; x < Region.WIDTH; x++) {
            for (int y = 0; y < Region.HEIGHT; y++) {
                if (region.tiles[x][y].air) spawnPoints.add(new Point(x, y));
            }
        }
        Point spawn = spawnPoints.get(RNG.range(0, spawnPoints.size() - 1));
        region.playerX = spawn.x + 0.5;
        region.playerY = spawn.y + 0.5;
        return region;
    }
    public static Ore randomOre(int region) {
        if (!RNG.chance(50)) return Registry.ORES.get("stone");
        ArrayList<Ore> availableOres = new ArrayList<>();
        for (Ore ore : ores) {
            if (ore.region <= region) {
                for (int i = 0; i < ore.spawnChance; i++) {
                    availableOres.add(ore);
                }
            }
        }
        return availableOres.get(RNG.range(0, availableOres.size() - 1));
    }
    public static Region createBossRoom() {
        Region region = new Region(4);
        region.spiderKillRequirement = 50;
        for (int x = 0; x < 240; x++) {
            for (int y = 0; y < 240; y++) {
                Tile tile = new Tile();
                tile.ore = Registry.ORES.get("black");
                tile.air = false;
                tile.light = false;
                region.tiles[x][y] = tile;
            }
        }
        for (int x = 0; x < 12; x++) {
            for (int y = 0; y < 12; y++) {
                Tile tile = region.tiles[117 + x][117 + y];
                tile.ore = Registry.ORES.get("stone");
                tile.air = true;
            }
        }
        int hallwayLength = 15;
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < hallwayLength; y++) {
                Tile tile = region.tiles[121 + x][129 + y];
                tile.ore = Registry.ORES.get("gray");
            }
        }
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                Tile tile = region.tiles[121 + x][129 + hallwayLength + y];
                tile.ore = Registry.ORES.get("stone");
                tile.air = true;
            }
        }
        region.playerX = 123;
        region.playerY = 131 + hallwayLength;
        region.noItems = true;
        return region;
    }
}
