package com.caveexp.gui.screens;

import com.badlogic.gdx.graphics.Texture;
import com.caveexp.Main;
import com.caveexp.assets.Loader;
import com.caveexp.game.Game;
import com.caveexp.gui.component.GUIComponent;
import com.caveexp.gui.component.PositionAnchor;
import com.caveexp.gui.font.Font;
import com.caveexp.gui.component.GUIButtonComponent;
import com.caveexp.util.RNG;
import com.caveexp.util.Renderer;
import com.caveexp.util.bjson.BJSONFile;
import java.awt.FileDialog;
import java.awt.Frame;

public class TitleScreen extends Screen {
    public static final String VERSION = "1.1";
    private static final Texture logo = Loader.get("logo", Loader.IMAGE);
    private static double logoScale;
    public void push() {
        if (Main.screenStack.size() > 1) return;
        Main.screenStack.popPush(new ScrollingBackgroundScreen());
        Main.screenStack.push(this);
    }
    public void update() {
        logoScale = getLogoScale();
        int y = 100 + (int)(logo.getHeight() * logoScale);
        int i = 0;
        for (GUIComponent component : getComponents()) {
            component.absY = y + i * 37;
            i++;
        }
    }
    public void init() {
        addComponent(GUIButtonComponent.build(-128, 0, 256, 32, "New Game", () -> {
            Game.newGame(RNG.random(), false);
            Main.screenStack.pop();
            Main.screenStack.popPush(new MainScreen());
        }).anchored(PositionAnchor.TOP_CENTER));
        addComponent(GUIButtonComponent.build(-128, 0, 256, 32, "Sandbox Mode", () -> {
            Game.newGame(RNG.random(), true);
            Main.screenStack.pop();
            Main.screenStack.popPush(new MainScreen());
        }).anchored(PositionAnchor.TOP_CENTER));
        addComponent(GUIButtonComponent.build(-128, 0, 256, 32, "Load Game", () -> {
            new Thread(() -> {
                FileDialog dialog = new FileDialog((Frame)null);
                dialog.setMode(FileDialog.LOAD);
                dialog.setTitle("Load Cave Exploration Save File");
                dialog.setVisible(true);
                if (dialog.getFiles().length == 0) return;
                Game.read(BJSONFile.read(dialog.getFiles()[0]));
                Main.screenStack.pop();
                Main.screenStack.popPush(new MainScreen());
            }).start();
        }).anchored(PositionAnchor.TOP_CENTER));
        addComponent(GUIButtonComponent.build(-128, 0, 256, 32, "Keybinds", () -> {
            Main.screenStack.popPush(new KeybindScreen(false));
        }).anchored(PositionAnchor.TOP_CENTER));
        addComponent(GUIButtonComponent.build(-128,  0, 256, 32, "Credits", () -> {
            Main.screenStack.popPush(new CreditsScreen());
        }).anchored(PositionAnchor.TOP_CENTER));
    }
    public void render(Renderer renderer) {
        renderer.setColor(0xFFFFFFFF);
        float width = (float)(logo.getWidth() * logoScale);
        float height = (float)(logo.getHeight() * logoScale);
        renderer.draw(logo, Main.windowWidth / 2f - width / 2f, 25, width, height);
        Font.render(renderer, Main.windowWidth / 2 - Font.stringWidth("Version " + VERSION) / 2, 30 + (int)height, "Version " + VERSION);
        Font.render(renderer, 5, Main.windowHeight - Font.getHeight() - 5, "Created by Dominicentek 2021-2022; Inspired by Minecraft");
    }
    private static double getLogoScale() {
        double aspectX = (double)Main.windowWidth / logo.getWidth();
        double aspectY = (double)Main.windowHeight / logo.getHeight();
        return Math.min(aspectX, aspectY) * 0.75;
    }
}
