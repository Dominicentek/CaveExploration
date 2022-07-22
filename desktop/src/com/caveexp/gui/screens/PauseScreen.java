package com.caveexp.gui.screens;

import com.badlogic.gdx.Gdx;
import com.caveexp.Main;
import com.caveexp.game.Game;
import com.caveexp.gui.component.GUIButtonComponent;
import com.caveexp.gui.component.PositionAnchor;
import com.caveexp.util.Input;
import com.caveexp.util.Renderer;
import com.caveexp.util.bjson.BJSONFile;
import java.awt.FileDialog;
import java.awt.Frame;

public class PauseScreen extends CloseableScreen {
    public void push() {
        Game.isPaused = true;
    }
    public void pop() {
        Game.isPaused = false;
    }
    public void init() {
        int buttons = 7;
        GUIButtonComponent seedButton = GUIButtonComponent.build(-128, -(buttons * 29 - 5) / 2 + 29 * 6, 256, 24, "$nCopy Seed: " + Game.seed, () -> {
            Gdx.app.getClipboard().setContents("" + Game.seed);
        });
        addComponent(GUIButtonComponent.build(-128, -(buttons * 29 - 5) / 2, 256, 24, "$nBack", Main.screenStack::pop).anchored(PositionAnchor.CENTER));
        addComponent(GUIButtonComponent.build(-128, -(buttons * 29 - 5) / 2 + 29, 256, 24, "$nSave", () -> {
            new Thread(() -> {
                try {
                    FileDialog dialog = new FileDialog((Frame)null);
                    dialog.setMode(FileDialog.SAVE);
                    dialog.setTitle("Save Cave Exploration Save File");
                    dialog.setAlwaysOnTop(true);
                    dialog.setVisible(true);
                    if (dialog.getFiles().length == 0) return;
                    BJSONFile.write(dialog.getFiles()[0], Game.write());
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }).anchored(PositionAnchor.CENTER));
        addComponent(GUIButtonComponent.build(-128, -(buttons * 29 - 5) / 2 + 29 * 2, 256, 24, "$nLoad", () -> {
            new Thread(() -> {
                try {
                    FileDialog dialog = new FileDialog((Frame)null);
                    dialog.setMode(FileDialog.LOAD);
                    dialog.setTitle("Load Cave Exploration Save File");
                    dialog.setAlwaysOnTop(true);
                    dialog.setVisible(true);
                    if (dialog.getFiles().length == 0) return;
                    Game.read(BJSONFile.read(dialog.getFiles()[0]));
                    seedButton.text = "$nCopy Seed: " + Game.seed;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
        }).anchored(PositionAnchor.CENTER));
        addComponent(GUIButtonComponent.build(-128, -(buttons * 29 - 5) / 2 + 29 * 3, 256, 24, "$nNew", () -> {
            SeedInputScreen seed = new SeedInputScreen();
            seed.onClose = () -> {
                Game.newGame(seed.getSeed(), false);
                seedButton.text = "$nCopy Seed: " + Game.seed;
            };
            Main.screenStack.push(seed);
        }).anchored(PositionAnchor.CENTER));
        addComponent(GUIButtonComponent.build(-128, -(buttons * 29 - 5) / 2 + 29 * 4, 256, 24, "$nQuit", () -> {
            Main.screenStack.clear();
            Main.screenStack.push(new TitleScreen());
        }).anchored(PositionAnchor.CENTER));
        addComponent(GUIButtonComponent.build(-128, -(buttons * 29 - 5) / 2 + 29 * 5, 256, 24, "$nAchievements", () -> {
            Main.screenStack.popPush(new AchievementScreen(this, true));
        }).anchored(PositionAnchor.CENTER));
        addComponent(seedButton.anchored(PositionAnchor.CENTER));
    }
    public void update() {
        if (Input.PAUSE.isJustPressed()) Main.screenStack.pop();
    }
    public void render(Renderer renderer) {
        renderer.setColor(0x0000007F);
        renderer.rectfill(0, 0, Main.windowWidth, Main.windowHeight);
    }
}
