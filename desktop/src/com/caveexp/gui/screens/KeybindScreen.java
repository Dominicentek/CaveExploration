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
    public void init() {
        GUIButtonComponent doneButton = GUIButtonComponent.build(-128, -37, 256, 32, "$nDone", () -> {
            Input.save();
            Main.screenStack.popPush(new TitleScreen());
        });
        int i = 0;
        for (Input input : Input.values()) {
            GUIButtonComponent button = GUIButtonComponent.build(10, 100 + i * 37, 128, 32, "$n" + input.toString(), null);
            button.action = () -> {
                button.text = "$n...";
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
            button.text = "$n" + awaitingInput;
            awaitingInput = null;
            button = null;
        }
    }
    public void buttonPress(int btn) {
        if (awaitingInput != null) {
            awaitingInput.key = btn;
            awaitingInput.button = true;
            button.text = "$n" + awaitingInput;
            awaitingInput = null;
            button = null;
        }
    }
    public void render(Renderer renderer) {
        Font.render(renderer, Main.windowWidth / 2 - Font.stringWidth("Change Keybinds") / 2, 25, "Change Keybinds");
        int i = 0;
        for (Input input : Input.values()) {
            Font.render(renderer, Main.windowWidth / 2 - 10 - Font.stringWidth(input.name), 100 + i * 37 + 16 - Font.getHeight() / 2, input.name);
            i++;
        }
    }
}
