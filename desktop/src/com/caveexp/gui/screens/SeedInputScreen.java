package com.caveexp.gui.screens;

import com.badlogic.gdx.Gdx;
import com.caveexp.Main;
import com.caveexp.gui.font.Font;
import com.caveexp.util.RNG;
import com.caveexp.util.Renderer;

public class SeedInputScreen extends CloseableScreen {
    private String seed = "";
    public Runnable onClose = () -> {};
    public void paste() {
        seed += Gdx.app.getClipboard().getContents();
    }
    public void key(char character) {
        if (character == '\b' && seed.length() != 0) seed = seed.substring(0, seed.length() - 1);
        if (character == '\n') {
            Main.screenStack.pop();
            onClose.run();
        }
        if (character < 32 || character > 126) return;
        seed += character;
    }
    public void render(Renderer renderer) {
        String seed = "$cFFFFFFSeed Input: " + this.seed + (System.currentTimeMillis() % 1000 < 500 ? "|" : "");
        int width = Font.stringWidth("Seed Input: " + this.seed + "|") + 16;
        int x = Main.windowWidth / 2 - width / 2;
        int y = Main.windowHeight / 2 - Font.getHeight() / 2;
        renderer.setColor(0x0000007F);
        renderer.rectfill(x - 8, y - 8, width, Font.getHeight() + 16);
        renderer.setColor(0xFFFFFFFF);
        renderer.rectdraw(x - 8, y - 8, width, Font.getHeight() + 16);
        Font.render(renderer, x, y, seed);
    }
    public long getSeed() {
        String seed = this.seed.trim();
        if (seed.length() == 0) return RNG.random();
        try {
            return Long.parseLong(seed);
        }
        catch (NumberFormatException e) {
            return seed.hashCode();
        }
    }
}
