package com.caveexp.gui.screens;

import com.caveexp.Main;
import com.caveexp.game.Game;
import com.caveexp.gui.component.GUIButtonComponent;
import com.caveexp.gui.component.PositionAnchor;
import com.caveexp.gui.font.Font;
import com.caveexp.util.RNG;
import com.caveexp.util.Renderer;
import com.caveexp.util.bjson.BJSONFile;
import java.awt.FileDialog;
import java.awt.Frame;

public class GameOverScreen extends Screen {
    public void push() {
        Game.isPaused = true;
    }
    public void init() {
        int buttons = 4;
        int y = -(buttons * 37 - 5) / 2;
        addComponent(GUIButtonComponent.build(-128, y, 256, 32, "New Game", () -> {
            Game.newGame(RNG.random(), false);
            Main.screenStack.pop();
        }).anchored(PositionAnchor.CENTER));
        addComponent(GUIButtonComponent.build(-128, y + 37, 256, 32, "Sandbox Mode", () -> {
            Game.newGame(RNG.random(), true);
            Main.screenStack.pop();
        }).anchored(PositionAnchor.CENTER));
        addComponent(GUIButtonComponent.build(-128, y + 37 * 2, 256, 32, "Load Game", () -> {
            new Thread(() -> {
                FileDialog dialog = new FileDialog((Frame)null);
                dialog.setMode(FileDialog.LOAD);
                dialog.setTitle("Load Cave Exploration Save File");
                dialog.setVisible(true);
                if (dialog.getFiles().length == 0) return;
                Game.read(BJSONFile.read(dialog.getFiles()[0]));
                Main.screenStack.pop();
            }).start();
        }).anchored(PositionAnchor.CENTER));
        addComponent(GUIButtonComponent.build(-128, y + 37 * 3, 256, 32, "Quit", () -> {
            Main.screenStack.pop();
            Main.screenStack.popPush(new TitleScreen());
        }).anchored(PositionAnchor.CENTER));
    }
    public void render(Renderer renderer) {
        float scale = 4;
        renderer.setColor(0xFF00007F);
        renderer.rectfill(0, 0, Main.windowWidth, Main.windowHeight);
        renderer.setColor(0xFFFFFFFF);
        Font.render(renderer, (int)(Main.windowWidth / 2 - Font.stringWidth("Game Over") / 2 * scale), 100, "Game Over", scale);
        String score = "Score: $cFFFF00" + Game.spidersKilled;
        Font.render(renderer, Main.windowWidth / 2 - Font.stringWidth(score) / 2, 200, score);
    }
}
