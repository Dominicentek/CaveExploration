package com.caveexp.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class ImageAsset extends Asset<Texture> {
    public ImageAsset(String path) {
        super(path);
    }
    public Texture load() {
        return new Texture(Gdx.files.internal(path));
    }
    public Texture placeholder() {
        Pixmap pixmap = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        for (int x = 0; x < pixmap.getWidth(); x++) {
            for (int y = 0; y < pixmap.getHeight(); y++) {
                int color = 0x000000FF;
                if ((x < 8 && y < 8) || (x >= 8 && y >= 8)) color = 0xFF00FFFF;
                pixmap.drawPixel(x, y, color);
            }
        }
        return new Texture(pixmap);
    }
    public void dispose() {
        asset.dispose();
    }
}
