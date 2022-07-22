package com.caveexp.gui.screens;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.caveexp.Main;
import com.caveexp.assets.Loader;
import com.caveexp.game.Game;
import com.caveexp.game.region.Region;
import com.caveexp.gui.font.Font;
import com.caveexp.util.Input;
import com.caveexp.util.Registry;
import com.caveexp.util.Renderer;

public class RegionSelectScreen extends CloseableScreen {
    private static final int SPEED = 4;
    private static final int SPACING = 100;
    private Texture[] textures = new Texture[5];
    private int pos = 0;
    private int dir = 0;
    private boolean facingLeft = false;
    public void push() {
        pos = Game.currentRegion.id * (Region.WIDTH + SPACING);
        for (int i = 0; i < Region.REGIONS; i++) {
            Pixmap region = new Pixmap(Region.WIDTH, Region.HEIGHT, Pixmap.Format.RGBA8888);
            for (int x = 0; x < region.getWidth(); x++) {
                for (int y = 0; y < region.getHeight(); y++) {
                    int alpha = 63 + (Region.regions[i].tiles[x][y].air ? 0 : 128) + (Region.regions[i].tiles[x][y].light ? 64 : 0);
                    region.drawPixel(x, y, i == 3 ? 0x00007FFF : 0x000000FF);
                    region.drawPixel(x, y, (Region.regions[i].tiles[x][y].ore.color & 0xFFFFFF00) + alpha);
                    region.drawPixel(x, y, Region.regions[i].tiles[x][y].item.color);
                }
            }
            textures[i] = new Texture(region);
        }
    }
    public void pop() {
        for (int i = 0; i < 5; i++) {
            textures[i].dispose();
            textures[i] = null;
        }
    }
    public void update() {
        int spacing = Region.WIDTH + SPACING;
        int region = pos / spacing;
        if (dir != 0) {
            pos += dir * SPEED;
            if (pos % spacing == 0) dir = 0;
            return;
        }
        if (Input.LEFT.isPressed() && region != 0) {
            dir = -1;
            facingLeft = true;
        }
        if (Input.RIGHT.isPressed() && region != Game.getUnlockedRegionID()) {
            dir = 1;
            facingLeft = false;
        }
        if (Input.TRAVEL_TO_REGION.isPressed()) {
            if (region != Game.currentRegion.id) Game.grantAchievement(Registry.ACHIEVEMENTS.get("region_travel"));
            if (region == 2) Game.grantAchievement(Registry.ACHIEVEMENTS.get("dark"));
            Game.currentRegion = Region.regions[region];
            Main.screenStack.pop();
        }
    }
    public void render(Renderer renderer) {
        renderer.setColor(0x000000AF);
        renderer.rectfill(0, 0, Main.windowWidth, Main.windowHeight);
        renderer.setColor(0xFFFFFFFF);
        int i = 0;
        for (Texture regionTexture : textures) {
            int x = Main.windowWidth / 2 - pos + i * (Region.WIDTH + SPACING);
            int y = Main.windowHeight / 2 - Region.HEIGHT / 2;
            renderer.draw(regionTexture, x - Region.WIDTH / 2, y);
            Font.render(renderer, x - Font.stringWidth("Region " + (i + 1)) / 2, y - 50 - Font.getHeight(), "Region " + (i + 1));
            if (Region.regions[i].spiderKillRequirement > Game.spidersKilled) {
                renderer.setColor(0xFFFFFF7F);
                renderer.rectfill(x - Region.WIDTH / 2, y, Region.WIDTH, Region.HEIGHT);
                renderer.setColor(0xFFFFFFFF);
                String[] text = ("Spiders killed\n" + Game.spidersKilled + "/" + Region.regions[i].spiderKillRequirement + "\nto unlock this region").split("\n");
                int Y = Main.windowHeight / 2 - (int)(text.length / 2.0 * Font.getHeight());
                int j = 0;
                for (String line : text) {
                    Font.render(renderer, x - Font.stringWidth(line) / 2, Y + j * Font.getHeight(), "$n$c000000" + line);
                    j++;
                }
            }
            i++;
        }
        renderer.draw(Loader.get("player" + (System.currentTimeMillis() % 500 < 250 && dir != 0 ? "walk" : ""), Loader.IMAGE), Main.windowWidth / 2 - 16, Main.windowHeight / 2 - 16, 32, 32, facingLeft, false);
    }
}
