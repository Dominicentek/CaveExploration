package com.caveexp.gui.screens;

import com.badlogic.gdx.math.Matrix4;
import com.caveexp.Main;
import com.caveexp.game.Game;
import com.caveexp.game.region.Region;
import com.caveexp.game.region.RegionGenerator;
import com.caveexp.util.Renderer;

public class ScrollingBackgroundScreen extends Screen {
    public static int scroll = 0;
    public Region region;
    public void init() {
        region = RegionGenerator.generateRegion(5);
    }
    public void update() {
        scroll++;
        if (scroll == Region.HEIGHT * 32) scroll = 0;
    }
    public void push() {
        Game.currentRegion = region;
    }
    public void render(Renderer renderer) {
        Game.render(renderer, false, Region.WIDTH * 16, scroll);
        renderer.setTransformMatrix(new Matrix4().setToTranslation(0, -Region.HEIGHT * 32 + scroll, 0));
        Game.render(renderer, false, Region.WIDTH * 16, 0);
        renderer.setTransformMatrix(new Matrix4());
        renderer.setColor(0x0000007F);
        renderer.rectfill(0, 0, Main.windowWidth, Main.windowHeight);
    }
}
