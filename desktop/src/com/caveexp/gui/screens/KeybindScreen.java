package com.caveexp.gui.screens;

import com.caveexp.Main;
import com.caveexp.gui.component.GUIButtonComponent;
import com.caveexp.gui.component.PositionAnchor;
import com.caveexp.gui.font.Font;
import com.caveexp.util.Input;
import com.caveexp.util.Renderer;
import javafx.geometry.Pos;

public class KeybindScreen extends Screen {
    private Input awaitingInput = null;
    private GUIButtonComponent button = null;
    private boolean ingame = false;
    public KeybindScreen(boolean ingame) {
        this.ingame = ingame;
    }
    public void init() {
        GUIButtonComponent doneButton = GUIButtonComponent.build(-128, -37, 256, 32, "Done", () -> {
            Input.save();
            Main.screenStack.popPush(ingame ? new PauseScreen() : new TitleScreen());
        });
        int i = 0;
        for (Input input : Input.values()) {
            GUIButtonComponent button = GUIButtonComponent.build(10, 100 + i * 37, 128, 32, input.toString(), null);
            button.action = () -> {
                button.text = "...";
                awaitingInput = input;
                this.button = button;
            };
            addComponent(button.anchored(PositionAnchor.TOP_CENTER));
            i++;
        }
        addComponent(doneButton.anchored(PositionAnchor.BOTTOM_CENTER));
    }
    public void keyPress(int key) {
        if (awaitingInput != null) {
            awaitingInput.key = key;
            awaitingInput.button = false;
            button.text = awaitingInput.toString();
            awaitingInput = null;
            button = null;
        }
    }
    public void buttonPress(int btn) {
        if (awaitingInput != null) {
            awaitingInput.key = btn;
            awaitingInput.button = true;
            button.text = awaitingInput.toString();
            awaitingInput = null;
            button = null;
        }
    }
    public void render(Renderer renderer) {
        if (ingame) {
            renderer.setColor(0x0000007F);
            renderer.rectfill(0, 0, Main.windowWidth, Main.windowHeight);
            renderer.setColor(0xFFFFFFFF);
        }
        Font.render(renderer, Main.windowWidth / 2 - Font.stringWidth("Change Keybinds") / 2, 25, "Change Keybinds");
        int i = 0;
        for (Input input : Input.values()) {
            Font.render(renderer, Main.windowWidth / 2 - 10 - Font.stringWidth(input.name), 100 + i * 37 + 16 - Font.getHeight() / 2, input.name);
            i++;
        }
    }
}
