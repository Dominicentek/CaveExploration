package com.caveexp.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.caveexp.Main;
import com.caveexp.assets.Loader;

import java.awt.Rectangle;

public class Renderer extends SpriteBatch {
    public Rectangle visible = null;
    public void draw(Texture texture, float x, float y) {
        draw(texture, x, y, texture.getWidth(), texture.getHeight());
    }
    public void draw(Texture texture, float x, float y, float w, float h) {
        draw(texture, x, y, w, h, false, false);
    }
    public void draw(Texture texture, float x, float y, float w, float h, boolean flipX, boolean flipY) {
        y = Main.windowHeight - y - h;
        if (visible == null) super.draw(texture, x, y, w, h, 0, 0, texture.getWidth(), texture.getHeight(), flipX, flipY);
        else {
            Rectangle region = visible.intersection(new Rectangle((int)x, (int)y, (int)w, (int)h));
            if (region.isEmpty()) return;
            int X = region.x - (int)x;
            int Y = region.y - (int)y;
            int width = (int)Math.ceil(texture.getWidth() / w * region.width);
            int height = (int)Math.ceil(texture.getHeight() / h * region.height);
            super.draw(texture, region.x, region.y, region.width, region.height, X, Y, width, height, flipX, flipY);
        }
    }
    public void rectfill(int x, int y, int w, int h) {
        draw(Loader.get("pixel", Loader.IMAGE), x, y, w, h);
    }
    public void rectdraw(int x, int y, int w, int h) {
        draw(Loader.get("pixel", Loader.IMAGE), x, y, w, 1);
        draw(Loader.get("pixel", Loader.IMAGE), x + w - 1, y, 1, h);
        draw(Loader.get("pixel", Loader.IMAGE), x, y + h - 1, w, 1);
        draw(Loader.get("pixel", Loader.IMAGE), x, y, 1, h);
    }
    public void setColor(int color) {
        int r = (color >> 24) & 0xFF;
        int g = (color >> 16) & 0xFF;
        int b = (color >> 8) & 0xFF;
        int a = color & 0xFF;
        setColor(r / 255f, g / 255f, b / 255f, a / 255f);
    }
    public void setVisibleRect(Rectangle visible) {
        this.visible = visible;
    }
}