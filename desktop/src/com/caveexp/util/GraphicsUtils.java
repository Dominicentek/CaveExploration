package com.caveexp.util;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import java.awt.Color;

public class GraphicsUtils {
    public static int getHue() {
        return (Color.HSBtoRGB((System.currentTimeMillis() % 5000) / 5000f, 1f, 1f) << 8) | 0xFF;
    }
    public static int darker(int rgb) {
        return brightness(rgb, 0.5f);
    }
    public static int brightness(int rgb, float factor) {
        int a = rgb & 0xFF;
        Color color = new Color(0xFF000000 | (rgb >>> 8));
        color = new Color(Math.min(255, (int)(color.getRed() * factor)), Math.min(255, (int)(color.getGreen() * factor)), Math.min(255, (int)(color.getBlue() * factor)));
        rgb = color.getRGB();
        rgb <<= 8;
        rgb |= a;
        return rgb;
    }
    public static Texture assemble(Texture tex1, Texture tex2, int tint) {
        Pixmap pixmap1 = toPixmap(tex1);
        Pixmap tinted = tint(toPixmap(tex2), tint);
        pixmap1.drawPixmap(tinted, 0, 0, tex2.getWidth(), tex2.getHeight(), 0, 0, tex1.getWidth(), tex1.getHeight());
        tinted.dispose();
        return new Texture(pixmap1);
    }
    public static Pixmap tint(Pixmap pixmap, int tint) {
        int r = (tint >> 24) & 0xFF;
        int g = (tint >> 16) & 0xFF;
        int b = (tint >> 8) & 0xFF;
        int a = tint & 0xFF;
        Pixmap tinted = new Pixmap(pixmap.getWidth(), pixmap.getHeight(), Pixmap.Format.RGBA8888);
        for (int x = 0; x < pixmap.getWidth(); x++) {
            for (int y = 0; y < pixmap.getHeight(); y++) {
                int rgb = pixmap.getPixel(x, y);
                int R = (rgb >> 24) & 0xFF;
                int G = (rgb >> 16) & 0xFF;
                int B = (rgb >> 8) & 0xFF;
                int A = rgb & 0xFF;
                R = (int)((R / 255f) * (r / 255f) * 255) & 0xFF;
                G = (int)((G / 255f) * (g / 255f) * 255) & 0xFF;
                B = (int)((B / 255f) * (b / 255f) * 255) & 0xFF;
                A = (int)((A / 255f) * (a / 255f) * 255) & 0xFF;
                tinted.drawPixel(x, y, (R << 24) | (G << 16) | (B << 8) | A);
            }
        }
        return tinted;
    }
    public static Pixmap toPixmap(Texture texture) {
        TextureData data = texture.getTextureData();
        if (!data.isPrepared()) data.prepare();
        return data.consumePixmap();
    }
}
