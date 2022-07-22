package com.caveexp.gui.screens;

import com.caveexp.Main;
import com.caveexp.gui.component.GUIButtonComponent;
import com.caveexp.gui.component.PositionAnchor;
import com.caveexp.gui.font.Font;
import com.caveexp.util.Input;
import com.caveexp.util.Renderer;

public class CreditsScreen extends Screen {
    public void init() {
        addComponent(GUIButtonComponent.build(-128, -37, 256, 32, "$nDone", () -> {
            Main.screenStack.popPush(new TitleScreen());
        }).anchored(PositionAnchor.BOTTOM_CENTER));
    }
    public void render(Renderer renderer) {
        String credits = "Programming and Design\n" +
                         "$bDominicentek\n" +
                         "\n" +
                         "Sprites and Textures\n" +
                         "$bBatata Douce\n" +
                         "\n" +
                         "Sound Effects\n" +
                         "$bTaken from Minecraft";
        int i = 0;
        for (String line : credits.split("\n")) {
            Font.render(renderer, Main.windowWidth / 2 - Font.stringWidth(line) / 2, 100 + i * Font.getHeight(), line);
            i++;
        }
    }
}
