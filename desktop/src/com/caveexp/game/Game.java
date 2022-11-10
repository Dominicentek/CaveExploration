package com.caveexp.game;

import com.caveexp.Main;
import com.caveexp.assets.Loader;
import com.caveexp.game.achievements.Achievement;
import com.caveexp.game.inventory.Inventory;
import com.caveexp.game.inventory.InventoryItem;
import com.caveexp.game.item.*;
import com.caveexp.game.ores.Ore;
import com.caveexp.game.region.*;
import com.caveexp.gui.font.Font;
import com.caveexp.gui.screens.*;
import com.caveexp.gui.masking.Circle;
import com.caveexp.util.*;
import com.caveexp.util.bjson.ListElement;
import com.caveexp.util.bjson.ObjectElement;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Game {
    public static Region currentRegion = Region.regions[0];
    public static boolean facingLeft = false;
    public static double prevX = 0;
    public static double prevY = 0;
    public static int selectedSlot = 0;
    public static Inventory inventory = new Inventory();
    public static int health = 15;
    public static double accelerationIce = 0.0015;
    public static double acceleration = 0.05;
    public static double speedX = 0;
    public static double speedY = 0;
    public static double maxSpeed = 0.05;
    public static long seed = RegionGenerator.seed;
    public static int spidersKilled;
    public static boolean isPaused = false;
    public static boolean sandboxMode = false;
    public static int maxDamage = 1;
    public static boolean noCollision = false;
    public static SpiderFight giantSpiderFight = new SpiderFight(-1, -1, 500, ((Sword)Registry.ITEMS.get("sword_platinum")).damage / 2);
    public static void update() {
        if (isPaused) return;
        updateCam();
        int camX = (int)Camera.windowX;
        int camY = (int)Camera.windowY;
        if (Main.screenStack.peek() instanceof MainScreen) {
            double acceleration = Region.regions[3] == currentRegion ? accelerationIce : Game.acceleration;
            if (Input.LEFT.isPressed() && !Input.RIGHT.isPressed()) {
                speedX -= acceleration;
                if (speedX < -maxSpeed) speedX = -maxSpeed;
                facingLeft = true;
            }
            else if (Input.RIGHT.isPressed() && !Input.LEFT.isPressed()) {
                speedX += acceleration;
                if (speedX > maxSpeed) speedX = maxSpeed;
                facingLeft = false;
            }
            else {
                if (speedX < 0) {
                    speedX += acceleration;
                    if (speedX > 0) speedX = 0;
                }
                if (speedX > 0) {
                    speedX -= acceleration;
                    if (speedX < 0) speedX = 0;
                }
            }
            if (Input.UP.isPressed() && !Input.DOWN.isPressed()) {
                speedY -= acceleration;
                if (speedY < -maxSpeed) speedY = -maxSpeed;
            }
            else if (Input.DOWN.isPressed() && !Input.UP.isPressed()) {
                speedY += acceleration;
                if (speedY > maxSpeed) speedY = maxSpeed;
            }
            else {
                if (speedY < 0) {
                    speedY += acceleration;
                    if (speedY > 0) speedY = 0;
                }
                if (speedY > 0) {
                    speedY -= acceleration;
                    if (speedY < 0) speedY = 0;
                }
            }
            currentRegion.playerX += speedX;
            if (insideWall()) {
                currentRegion.playerX -= speedX;
                speedX = 0;
            }
            currentRegion.playerY += speedY;
            if (insideWall()) {
                currentRegion.playerY -= speedY;
                speedY = 0;
            }
            currentRegion.playerX = Math.round(currentRegion.playerX * 100) / 100.0;
            currentRegion.playerY = Math.round(currentRegion.playerY * 100) / 100.0;
            selectedSlot += Input.scroll;
            if (selectedSlot < 0) {
                while (selectedSlot < 0) {
                    selectedSlot += 10;
                }
            }
            if (selectedSlot >= 10) {
                while (selectedSlot >= 10) {
                    selectedSlot -= 10;
                }
            }
            for (InventoryItem invItem : inventory.items) {
                Item item = Registry.ITEMS.get(invItem.item);
                if (item instanceof Sword) {
                    maxDamage = Math.max(maxDamage, ((Sword)item).damage);
                }
            }
            if (Input.ACTION.isJustPressed()) {
                if (inventory.items[selectedSlot].item.equals("potion") && !sandboxMode) {
                    inventory.removeItem(new InventoryItem("potion"));
                    health += 5;
                    if (health > 15) health = 15;
                    grantAchievement(Registry.ACHIEVEMENTS.get("drink_potion"));
                    Registry.SOUNDS.get("drink").play();
                }
                int tileX = (Input.mouseX + camX) / 32;
                int tileY = (Input.mouseY + camY) / 32;
                double distance = Math.sqrt(Math.pow(currentRegion.playerX - tileX, 2) + Math.pow(currentRegion.playerY - tileY, 2));
                int giantSpiderX = 121 * 32 - camX;
                int giantSpiderY = 121 * 32 - camY;
                if (currentRegion.id == Region.REGIONS - 1 && Input.mouseX >= giantSpiderX && Input.mouseY >= giantSpiderY && Input.mouseX <= giantSpiderX + 128 && Input.mouseY <= giantSpiderY + 128 && Registry.ACHIEVEMENTS.get("big_spider").isAchieved()) {
                    boolean defeated = giantSpiderFight.attack(Registry.ITEMS.get(inventory.items[selectedSlot].item));
                    if (defeated) {
                        grantAchievement(Registry.ACHIEVEMENTS.get("did_i_win"));
                    }
                }
                if (tileX < 0 || tileY < 0 || tileX >= Region.WIDTH || tileY >= Region.HEIGHT) return;
                if (distance > 3 && !sandboxMode && !inventory.items[selectedSlot].item.equals("pickaxe_croc")) return;
                if (currentRegion.tiles[tileX][tileY].item == TileItem.SPIDER) {
                    SpiderFight fight = getFight(tileX, tileY);
                    if (fight == null) {
                        fight = new SpiderFight(tileX, tileY, getUnlockedRegionID() * 10 + (spidersKilled < 1 ? 5 : 10), (int)Math.round(maxDamage * 0.5));
                        currentRegion.activeFights.add(fight);
                        return;
                    }
                    boolean defeated = fight.attack(Registry.ITEMS.get(inventory.items[selectedSlot].item));
                    if (defeated) {
                        currentRegion.activeFights.remove(fight);
                        spidersKilled++;
                        currentRegion.tiles[tileX][tileY].item = TileItem.NOTHING;
                    }
                }
                else if (currentRegion.tiles[tileX][tileY].item.placeItem != null) {
                    InventoryItem item = new InventoryItem(Registry.ITEMS.getID(currentRegion.tiles[tileX][tileY].item.placeItem));
                    if (inventory.cannotBeAdded(item)) return;
                    if (!sandboxMode) inventory.addItem(item);
                    currentRegion.tiles[tileX][tileY].item.placeItem.sound.play();
                    currentRegion.tiles[tileX][tileY].item = TileItem.NOTHING;
                    currentRegion.updateLight();
                }
                else if (Registry.ITEMS.get(inventory.items[selectedSlot].item) instanceof PlaceItem && currentRegion.tiles[tileX][tileY].air) {
                    currentRegion.tiles[tileX][tileY].item = ((PlaceItem)Registry.ITEMS.get(inventory.items[selectedSlot].item)).item;
                    ((PlaceItem)Registry.ITEMS.get(inventory.items[selectedSlot].item)).sound.play();
                    if (!sandboxMode) inventory.removeItem(new InventoryItem(inventory.items[selectedSlot].item));
                    currentRegion.updateLight();
                }
                else if (Registry.ITEMS.get(inventory.items[selectedSlot].item) instanceof OreItem && sandboxMode) {
                    currentRegion.tiles[tileX][tileY].ore = ((OreItem)Registry.ITEMS.get(inventory.items[selectedSlot].item)).ore;
                    Registry.SOUNDS.get("destroy").play();
                }
                else {
                    Tile tile = currentRegion.tiles[tileX][tileY];
                    if (tile.air) {
                        if (sandboxMode) tile.air = false;
                        return;
                    }
                    Ore ore = tile.ore;
                    if (ore == Registry.ORES.get("platinum")) grantAchievement(Registry.ACHIEVEMENTS.get("platinum"));
                    Item item = Registry.ITEMS.get(inventory.items[selectedSlot].item);
                    int tier = item instanceof Pickaxe ? ((Pickaxe)item).tier : 0;
                    InventoryItem drop = new InventoryItem(ore.drop);
                    if (((ore.tier > tier && tier != -1) || inventory.cannotBeAdded(drop)) && !sandboxMode) return;
                    if (tileY == 129 && currentRegion.id == 4) grantAchievement(Registry.ACHIEVEMENTS.get("big_spider"));
                    Registry.SOUNDS.get("destroy").play();
                    tile.air = true;
                    if (sandboxMode) return;
                    if (item instanceof Tool) ((Tool)item).damage();
                    inventory.addItem(drop);
                }
            }
            Rectangle hitbox = new Rectangle((int)(currentRegion.playerX * 32 - 14), (int)(currentRegion.playerY * 32 - 14), 28, 28);
            for (int x = Math.max(0, (int)currentRegion.playerX - 1); x <= Math.min(Region.WIDTH - 1, (int)currentRegion.playerX + 1); x++) {
                for (int y = Math.max(0, (int)currentRegion.playerY - 1); y <= Math.min(Region.HEIGHT - 1, (int)currentRegion.playerY + 1); y++) {
                    if (hitbox.intersects(new Rectangle(x * 32, y * 32, 32, 32))) {
                        if (currentRegion.tiles[x][y].item.pickup(inventory)) {
                            currentRegion.tiles[x][y].item = TileItem.NOTHING;
                        }
                    }
                }
            }
            if (Input.CRAFTING.isJustPressed()) Main.screenStack.push(new CraftingScreen());
            if (Input.REGION_SELECTOR.isJustPressed()) Main.screenStack.push(new RegionSelectScreen());
            if (Input.REMOVE_ITEM.isPressed()) {
                inventory.items[selectedSlot].item = "air";
                inventory.items[selectedSlot].count = 0;
            }
            if (Input.PAUSE.isJustPressed()) Main.screenStack.push(new PauseScreen());
            if (health <= 0) {
                health = 0;
                grantAchievement(Registry.ACHIEVEMENTS.get("game_over"));
                Main.screenStack.push(new GameOverScreen());
            }
            if (inventory.hasEnough(new InventoryItem("potion", 100))) grantAchievement(Registry.ACHIEVEMENTS.get("100_potions"));
            if (inventory.hasEnough(new InventoryItem("sword_dev", 1)) && inventory.hasEnough(new InventoryItem("pickaxe_dev", 1))) grantAchievement(Registry.ACHIEVEMENTS.get("developer"));
        }
        Main.mask.clear();
        if (Region.regions[2] == currentRegion) {
            Main.mask.add(new Circle((float)currentRegion.playerX * 32 - camX, (float)currentRegion.playerY * 32 - camY, 80));
            for (int x = 0; x < Region.WIDTH; x++) {
                for (int y = 0; y < Region.HEIGHT; y++) {
                    if (currentRegion.tiles[x][y].item == TileItem.TORCH) Main.mask.add(new Circle((float)x * 32 + 16 - camX, (float)y * 32 + 16 - camY, 112));
                }
            }
        }
        if (!currentRegion.noItems && !sandboxMode) spawnItem();
    }
    public static void render(Renderer renderer) {
        int camX = (int)Camera.windowX;
        int camY = (int)Camera.windowY;
        render(renderer, true, camX, camY);
    }
    public static void render(Renderer renderer, boolean drawPlayerAndHUD, int camX, int camY) {
        boolean noSpider = true;
        for (int x = Math.max(0, camX / 32 - 1); x <= Math.min(Region.WIDTH - 1, (camX + Main.windowWidth) / 32 + 1); x++) {
            for (int y = Math.max(0, camY / 32 - 1); y <= Math.min(Region.HEIGHT - 1, (camY + Main.windowHeight) / 32 + 1); y++) {
                Tile t = currentRegion.tiles[x][y];
                Ore ore = t.ore;
                int alpha = (t.air ? 63 : 191) + (t.light ? 64 : 0);
                renderer.setColor(0xFFFFFF00 | alpha);
                renderer.draw(ore.texture, x * 32 - camX, y * 32 - camY, 32, 32);
                if (t.item != TileItem.NOTHING) {
                    renderer.setColor(0xFFFFFFFF);
                    renderer.draw(Loader.get(t.item.name().toLowerCase(), Loader.IMAGE), x * 32 - camX, y * 32 - camY, 32, 32);
                }
                if (t.item == TileItem.SPIDER && Input.mouseX >= x * 32 - camX && Input.mouseY >= y * 32 - camY && Input.mouseX <= (x + 1) * 32 - camX && Input.mouseY <= (y + 1) * 32 - camY) {
                    SpiderFight fight = getFight(x, y);
                    if (fight == null) {
                        Main.hoverMessage = "You haven't fought this spider yet (Click on it to fight it)";
                        Main.hoverMessageSplit = 0;
                    }
                    else {
                        Main.hoverMessage = "Spider Health: " + fight.spiderHealth + "/" + fight.maxHealth;
                        Main.hoverMessageSplit = fight.spiderHealth / (double)fight.maxHealth;
                    }
                    noSpider = false;
                }
            }
        }
        if (currentRegion.id == Region.REGIONS - 1 && giantSpiderFight.spiderHealth > 0) {
            int giantSpiderX = 121 * 32 - camX;
            int giantSpiderY = 121 * 32 - camY;
            if (Input.mouseX >= giantSpiderX && Input.mouseY >= giantSpiderY && Input.mouseX <= giantSpiderX + 128 && Input.mouseY <= giantSpiderY + 128) {
                noSpider = false;
                Main.hoverMessageSplit = giantSpiderFight.spiderHealth / (double)giantSpiderFight.maxHealth;
                Main.hoverMessage = "Giant Enemy Spider Health: " + giantSpiderFight.spiderHealth + "/" + giantSpiderFight.maxHealth;
            }
            renderer.draw(Loader.get("spider", Loader.IMAGE), giantSpiderX, giantSpiderY, 128, 128, false, true);
        }
        if (noSpider) Main.hoverMessage = "";
        if (!drawPlayerAndHUD) return;
        boolean walking = prevX != currentRegion.playerX || prevY != currentRegion.playerY;
        prevX = currentRegion.playerX;
        prevY = currentRegion.playerY;
        boolean showWalkingSprite = walking && System.currentTimeMillis() % 500 < 250;
        renderer.setColor(0xFFFFFFFF);
        renderer.draw(Loader.get("player" + (showWalkingSprite ? "walk" : ""), Loader.IMAGE), (int)(currentRegion.playerX * 32 - 16) - camX, (int)(currentRegion.playerY * 32 - 16) - camY, 32, 32, facingLeft, false);
        Main.renderMask();
        renderer.setColor(0x0000007F);
        renderer.rectfill(5, 5, Inventory.SLOTS * 37 + 3, 40);
        renderer.setColor(0xFFFFFFFF);
        renderer.rectdraw(5, 5, Inventory.SLOTS * 37 + 3, 40);
        for (int i = 0; i < Inventory.SLOTS; i++) {
            if (i == selectedSlot) {
                renderer.setColor(0xFFFFFF7F);
                renderer.rectfill(7 + i * 37, 7, 36, 36);
            }
            renderer.setColor(0xFFFFFFFF);
            renderer.rectdraw(7 + i * 37, 7, 36, 36);
            renderer.draw(Registry.ITEMS.get(inventory.items[i].item).texture, 9 + i * 37, 9);
            if (inventory.items[i].count == 0) continue;
            Font.render(renderer, 41 + i * 37 - Font.stringWidth(inventory.items[i].count + ""), 44 - Font.getHeight(), inventory.items[i].count + "");
        }
        Font.render(renderer, 5, 50, Registry.ITEMS.get(inventory.items[selectedSlot].item).name);
        if (!sandboxMode) {
            int width = 256;
            int split = (int)((health / 15.0) * width);
            renderer.setColor(0xFF00007F);
            renderer.rectfill(Inventory.SLOTS * 37 + 13, 5, split, 40);
            renderer.setColor(0x0000007F);
            renderer.rectfill(Inventory.SLOTS * 37 + 13 + split, 5, width - split, 40);
            renderer.setColor(0xFFFFFFFF);
            renderer.rectdraw(Inventory.SLOTS * 37 + 13, 5, width, 40);
            String text = "Health: " + health + "/15";
            Font.render(renderer, Inventory.SLOTS * 37 + 13 + width / 2 - Font.stringWidth(text) / 2, 5 + 20 - Font.getHeight() / 2, text);
        }
        if (noCollision) Font.render(renderer, Inventory.SLOTS * 37 - Font.stringWidth("NoClip enabled"), 50, "NoClip enabled");
        int tileX = (Input.mouseX + camX) / 32;
        int tileY = (Input.mouseY + camY) / 32;
        double distance = Math.sqrt(Math.pow(currentRegion.playerX - tileX, 2) + Math.pow(currentRegion.playerY - tileY, 2));
        if (tileX < 0 || tileY < 0 || tileX >= 240 || tileY >= 240) return;
        String oreName = currentRegion.tiles[tileX][tileY].ore.name;
        Font.render(renderer, Main.windowWidth - 5 - Font.stringWidth(oreName), 5, oreName);
        if ((distance > 3 && !sandboxMode && !inventory.items[selectedSlot].item.equals("pickaxe_croc")) || !(Main.screenStack.peek() instanceof MainScreen)) return;
        renderer.setColor(0xFFFFFF7F);
        renderer.rectfill(tileX * 32 - camX, tileY * 32 - camY, 32, 32);
        renderer.setColor(0xFFFFFFFF);
    }
    public static boolean insideWall() {
        if (isOOB()) return true;
        if (noCollision) return false;
        Rectangle hitbox = new Rectangle((int)(currentRegion.playerX * 32 - 14), (int)(currentRegion.playerY * 32 - 14), 28, 28);
        for (int x = Math.max(0, (int)currentRegion.playerX - 1); x <= Math.min(239, (int)currentRegion.playerX + 1); x++) {
            for (int y = Math.max(0, (int)currentRegion.playerY - 1); y <= Math.min(239, (int)currentRegion.playerY + 1); y++) {
                if (hitbox.intersects(new Rectangle(x * 32, y * 32, 32, 32)) && (!currentRegion.tiles[x][y].air || currentRegion.tiles[x][y].item == TileItem.LAVA)) return true;
            }
        }
        return false;
    }
    public static boolean isOOB() {
        Rectangle innerRect = new Rectangle(Region.WIDTH * 32, Region.HEIGHT * 32);
        Rectangle hitbox = new Rectangle((int)(currentRegion.playerX * 32 - 14), (int)(currentRegion.playerY * 32 - 14), 28, 28);
        return !(innerRect.contains(hitbox.x, hitbox.y) && innerRect.contains(hitbox.x + hitbox.width, hitbox.y + hitbox.height));
    }
    public static void spawnItem() {
        if (!RNG.chance(50)) return;
        ArrayList<Point> freeSpaces = new ArrayList<>();
        int items = 0;
        for (int x = 0; x < Region.WIDTH; x++) {
            for (int y = 0; y < Region.WIDTH; y++) {
                Tile tile = currentRegion.tiles[x][y];
                if (tile.air && !tile.light) {
                    if (tile.item == TileItem.NOTHING) freeSpaces.add(new Point(x, y));
                    else items++;
                }
            }
        }
        if (items == 250) return;
        Point point = freeSpaces.get(RNG.range(0, freeSpaces.size() - 1));
        TileItem item = RNG.chance(2) ? TileItem.SPIDER : TileItem.STICK;
        if (RNG.chance(8)) item = TileItem.POTION;
        currentRegion.tiles[point.x][point.y].item = item;
    }
    public static SpiderFight getFight(int x, int y) {
        for (SpiderFight fight : currentRegion.activeFights) {
            if (fight.x == x && fight.y == y) return fight;
        }
        return null;
    }
    public static int getUnlockedRegionID() {
        int i = -1;
        for (Region region : Region.regions) {
            if (region.spiderKillRequirement > spidersKilled) return i;
            i++;
        }
        return i;
    }
    public static void grantAchievement(Achievement achievement) {
        if (achievement.isAchieved()) return;
        achievement.grant();
        Main.achievementToast(achievement);
    }
    public static ObjectElement write() {
        ObjectElement element = new ObjectElement();
        ListElement tiles = new ListElement();
        ListElement regions = new ListElement();
        for (int i = 0; i < Region.REGIONS; i++) {
            ObjectElement region = new ObjectElement();
            ListElement rows = new ListElement();
            for (int y = 0; y < Region.HEIGHT; y++) {
                ListElement row = new ListElement();
                for (int x = 0;  x < Region.WIDTH; x++) {
                    ObjectElement tileData = new ObjectElement();
                    tileData.setString("oreID", Registry.ORES.getID(Region.regions[i].tiles[x][y].ore));
                    tileData.setByte("item", (byte)Region.regions[i].tiles[x][y].item.ordinal());
                    tileData.setBoolean("air", Region.regions[i].tiles[x][y].air);
                    tileData.setBoolean("light", Region.regions[i].tiles[x][y].light);
                    if (tiles.indexOf(tileData) == -1) tiles.addObject(tileData);
                    row.addShort((short)tiles.indexOf(tileData));
                }
                rows.addList(row);
            }
            region.setList("data", rows);
            region.setFloat("x", (float)Region.regions[i].playerX);
            region.setFloat("y", (float)Region.regions[i].playerY);
            region.setBoolean("noItems", Region.regions[i].noItems);
            region.setByte("spiderKillRequirement", (byte)Region.regions[i].spiderKillRequirement);
            ListElement fights = new ListElement();
            for (SpiderFight spiderFight : Region.regions[i].activeFights) {
                fights.addObject(saveSpiderFight(spiderFight));
            }
            region.setList("fights", fights);
            regions.addObject(region);
        }
        element.setList("tiles", tiles);
        element.setList("regions", regions);
        element.setObject("giantSpiderFight", saveSpiderFight(giantSpiderFight));
        ObjectElement achievements = new ObjectElement();
        for (Achievement achievement : Registry.ACHIEVEMENTS) {
            achievements.setBoolean(Registry.ACHIEVEMENTS.getID(achievement), achievement.isAchieved());
        }
        element.setObject("achievements", achievements);
        ListElement inventory = new ListElement();
        for (int i = 0; i < Inventory.SLOTS; i++) {
            ObjectElement inventoryItem = new ObjectElement();
            InventoryItem item = Game.inventory.items[i];
            inventoryItem.setString("id", item.item);
            inventoryItem.setShort("count", (short)item.count);
            inventory.addObject(inventoryItem);
        }
        element.setList("inventory", inventory);
        element.setByte("selectedSlot", (byte)selectedSlot);
        element.setByte("health", (byte)health);
        element.setByte("currentRegion", (byte)currentRegion.id);
        element.setBoolean("facingLeft", facingLeft);
        element.setFloat("speedX", (float)speedX);
        element.setFloat("speedY", (float)speedY);
        element.setByte("spidersKilled", (byte)spidersKilled);
        element.setBoolean("sandbox", sandboxMode);
        element.setLong("seed", seed);
        return element;
    }
    public static void read(ObjectElement element) {
        ListElement tiles = element.getList("tiles");
        ListElement regions = element.getList("regions");
        for (int i = 0; i < regions.size(); i++) {
            ObjectElement region = regions.getObject(i);
            ListElement rows = region.getList("data");
            Region reg = new Region(i);
            for (int y = 0; y < rows.size(); y++) {
                ListElement row = rows.getList(y);
                for (int x = 0; x < row.size(); x++) {
                    ObjectElement tile = tiles.getObject(row.getShort(x));
                    Tile t = new Tile();
                    t.ore = Registry.ORES.get(tile.getString("oreID"));
                    t.item = TileItem.values()[tile.getByte("item")];
                    t.air = tile.getBoolean("air");
                    t.light = tile.getBoolean("light");
                    reg.tiles[x][y] = t;
                }
            }
            reg.playerX = region.getFloat("x");
            reg.playerY = region.getFloat("y");
            reg.noItems = region.getBoolean("noItems");
            reg.spiderKillRequirement = region.getByte("spiderKillRequirement");
            ListElement fights = region.getList("fights");
            for (int j = 0; j < fights.size(); j++) {
                ObjectElement fight = fights.getObject(i);
                reg.activeFights.add(readSpiderFight(fight));
            }
            Region.regions[i] = reg;
        }
        giantSpiderFight = readSpiderFight(element.getObject("giantSpiderFight"));
        ListElement inventory = element.getList("inventory");
        Inventory inv = new Inventory();
        for (int i = 0; i < Inventory.SLOTS; i++) {
            ObjectElement inventoryItem = inventory.getObject(i);
            InventoryItem item = new InventoryItem(inventoryItem.getString("id"), Short.toUnsignedInt(inventoryItem.getShort("count")));
            inv.putItem(item, i);
        }
        Game.inventory = inv;
        selectedSlot = element.getByte("selectedSlot");
        ObjectElement achievements = element.getObject("achievements");
        for (String id : achievements.keySet()) {
            if (achievements.getBoolean(id)) Registry.ACHIEVEMENTS.get(id).grant();
        }
        health = element.getByte("health");
        currentRegion = Region.regions[element.getByte("currentRegion")];
        facingLeft = element.getBoolean("facingLeft");
        speedX = element.getFloat("speedX");
        speedX = element.getFloat("speedY");
        spidersKilled = element.getByte("spidersKilled");
        sandboxMode = element.getBoolean("sandbox");
        isPaused = false;
        seed = element.getLong("seed");
    }
    public static ObjectElement saveSpiderFight(SpiderFight fight) {
        ObjectElement data = new ObjectElement();
        data.setByte("x", (byte)fight.x);
        data.setByte("y", (byte)fight.y);
        data.setByte("health", (byte)fight.spiderHealth);
        data.setByte("strength", (byte)fight.spiderStrength);
        return data;
    }
    public static SpiderFight readSpiderFight(ObjectElement fight) {
        return new SpiderFight(Byte.toUnsignedInt(fight.getByte("x")), Byte.toUnsignedInt(fight.getByte("y")), Byte.toUnsignedInt(fight.getByte("health")), Byte.toUnsignedInt(fight.getByte("strength")));
    }
    public static void revokeAchievement(Achievement achievement) {
        achievement.revoke();
    }
    public static void revokeAllAchievements() {
        for (Achievement achievement : Registry.ACHIEVEMENTS) {
            revokeAchievement(achievement);
        }
    }
    public static void updateCam() {
        Camera.setTarget(currentRegion.playerX * 32 - 16, currentRegion.playerY * 32 - 16);
        Camera.update();
    }
    public static void newGame(long seed, boolean sandbox) {
        RegionGenerator.generateRegions(seed);
        sandboxMode = sandbox;
        currentRegion = Region.regions[0];
        inventory = new Inventory();
        health = 15;
        selectedSlot = 0;
        facingLeft = false;
        prevX = currentRegion.playerX;
        prevY = currentRegion.playerY;
        spidersKilled = 0;
        isPaused = false;
        revokeAllAchievements();
        Game.seed = seed;
        updateCam();
        Camera.snap();
    }
}
