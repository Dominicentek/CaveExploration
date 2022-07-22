package com.caveexp.gui.font;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.caveexp.util.GraphicsUtils;
import com.caveexp.util.RNG;
import com.caveexp.util.Renderer;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;

public class Font {
    private static final ArrayList<FontGlyph> glyphs = new ArrayList<>();
    private static final HashMap<String, Texture> cache = new HashMap<>();
    private static int height = 0;
    private static int spacing = 1;
    public static FontGlyph getGlyphForChar(char character) {
        for (FontGlyph glyph : glyphs) {
            if (glyph.getCharacter() == character) return glyph;
        }
        return null;
    }
    public static FontGlyph getRandomGlyphWithWidth(int width) {
        ArrayList<FontGlyph> validGlyphs = new ArrayList<>();
        for (FontGlyph glyph : glyphs) {
            if (glyph.getWidth() == width || width == -1) validGlyphs.add(glyph);
        }
        return validGlyphs.get(RNG.range(0, validGlyphs.size() - 1));
    }
    public static int stringWidth(String string) {
        int width = 0;
        for (int i = 0; i < string.length(); i++) {
            char character = string.charAt(i);
            if (character == FontFormatting.prefix && i < string.length() - 1) {
                char formattingChar = string.charAt(i + 1);
                if (FontFormatting.isFormattingChar(formattingChar)) {
                    if (formattingChar == FontFormatting.COLOR.getCharacter() && i < string.length() - 7) i += 7;
                    else i++;
                }
            }
            else {
                width += getGlyphForChar(character).getWidth() + spacing;
            }
        }
        return width - spacing;
    }
    public static int getHeight() {
        return height;
    }
    public static void loadFromBinaryData(byte[] data) {
        ByteBuffer buffer = ByteBuffer.wrap(data);
        int height = Byte.toUnsignedInt(buffer.get());
        int minChar = Byte.toUnsignedInt(buffer.get());
        int maxChar = Byte.toUnsignedInt(buffer.get());
        spacing = Byte.toUnsignedInt(buffer.get());
        Font.height = height;
        for (int i = minChar; i <= maxChar; i++) {
            int width = Byte.toUnsignedInt(buffer.get());
            boolean[][] glyphData = new boolean[width][height];
            int bytesForGlyph = (int)Math.ceil(width * height / 8.0);
            boolean[] binaryData = byteToBinaryArray(buffer, bytesForGlyph);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    glyphData[x][y] = binaryData[y * width + x];
                }
            }
            glyphs.add(new FontGlyph(width, glyphData, (char)i));
        }
    }
    public static void render(Renderer renderer, int x, int y, String text) {
        render(renderer, x, y, text, 1);
    }
    public static void render(Renderer renderer, int x, int y, String text, float scale) {
        boolean dontCache = text.contains(FontFormatting.OBFUSCATED.toString()) || text.contains(FontFormatting.RAINBOW.toString());
        Texture font;
        if (!dontCache && cache.containsKey(text)) font = cache.get(text);
        else {
            int width = stringWidth(text) + 10;
            int height = getHeight() + 3;
            Pixmap fontPixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
            int offset = 0;
            int color = 0xFFFFFF;
            boolean bold = false;
            boolean italic = false;
            boolean underline = false;
            boolean strikethrough = false;
            boolean rainbow = false;
            boolean obfuscated = false;
            boolean noShadow = false;
            for (int i = 0; i < text.length(); i++) {
                char character = text.charAt(i);
                boolean exists = false;
                for (FontGlyph glyph : glyphs) {
                    if (glyph.getCharacter() == character) exists = true;
                }
                if (!exists) character = '?';
                if (character == FontFormatting.prefix && i < text.length() - 1) {
                    char formattingChar = text.charAt(i + 1);
                    if (FontFormatting.isFormattingChar(formattingChar)) {
                        if (formattingChar == FontFormatting.COLOR.getCharacter() && i < text.length() - 7) {
                            String clr = text.substring(i + 2, i + 8);
                            color = Integer.parseInt(clr, 16);
                            i += 6;
                        } else if (formattingChar == FontFormatting.BOLD.getCharacter()) bold = true;
                        else if (formattingChar == FontFormatting.ITALIC.getCharacter()) italic = true;
                        else if (formattingChar == FontFormatting.UNDERLINE.getCharacter()) underline = true;
                        else if (formattingChar == FontFormatting.STRIKETHROUGH.getCharacter()) strikethrough = true;
                        else if (formattingChar == FontFormatting.OBFUSCATED.getCharacter()) obfuscated = true;
                        else if (formattingChar == FontFormatting.RAINBOW.getCharacter()) rainbow = true;
                        else if (formattingChar == FontFormatting.NO_SHADOW.getCharacter()) noShadow = true;
                        else if (formattingChar == FontFormatting.RESET.getCharacter()) {
                            color = 0xFFFFFF;
                            bold = false;
                            italic = false;
                            underline = false;
                            strikethrough = false;
                            obfuscated = false;
                            rainbow = false;
                            noShadow = false;
                        }
                        i++;
                        continue;
                    }
                }
                FontGlyph glyph;
                if (obfuscated) glyph = getRandomGlyphWithWidth(getGlyphForChar(character).getWidth());
                else glyph = getGlyphForChar(character);
                boolean[][] data = glyph.getData();
                if (!noShadow) {
                    fontPixmap.setColor(GraphicsUtils.darker(rainbow ? GraphicsUtils.getHue() : (color << 8) | 0xFF));
                    for (int X = 0; X <= data.length; X++) {
                        for (int Y = 0; Y < data[0].length; Y++) {
                            int italicOffset = italic ? (data[0].length - 1 - Y) / 3 : 0;
                            if (X < data.length) {
                                if (data[X][Y]) fontPixmap.drawPixel(X + offset + 1 + italicOffset, Y + 1);
                                if (bold && data[X][Y]) fontPixmap.drawPixel(X + offset + 2 + italicOffset, Y + 1);
                            }
                            if (Y == 4 && strikethrough) fontPixmap.drawPixel(X + offset + 1, Y + 1);
                        }
                        if (underline) fontPixmap.drawPixel(X + offset + 1, height - 1);
                    }
                }
                fontPixmap.setColor(((rainbow ? GraphicsUtils.getHue() : color) << 8) | 0xFF);
                for (int X = 0; X <= data.length; X++) {
                    for (int Y = 0; Y < data[0].length; Y++) {
                        int italicOffset = italic ? (data[0].length - 1 - Y) / 3 : 0;
                        if (X < data.length) {
                            if (data[X][Y]) fontPixmap.drawPixel(X + offset + italicOffset, Y);
                            if (bold && data[X][Y]) fontPixmap.drawPixel(X + offset + 1 + italicOffset, Y);
                        }
                        if (Y == 4 && strikethrough) fontPixmap.drawPixel(X + offset, Y);
                    }
                    if (underline) fontPixmap.drawPixel(X + offset, height - 2);
                }
                offset += glyph.getWidth() + spacing;
            }
            font = new Texture(fontPixmap);
            if (!dontCache) cache.put(text, font);
        }
        renderer.draw(font, x, y, font.getWidth() * scale, font.getHeight() * scale);
    }
    public static void disposeCache() {
        for (String key : cache.keySet()) {
            cache.get(key).dispose();
        }
    }
    private static boolean[] byteToBinaryArray(ByteBuffer buffer, int length) {
        boolean[] binary = new boolean[length * 8];
        for (int i = 0; i < length; i++) {
            byte value = buffer.get();
            for (int j = 0; j < 8; j++) {
                binary[i * 8 + j] = ((value >> 7 - j) & 1) == 1;
            }
        }
        return binary;
    }
}
