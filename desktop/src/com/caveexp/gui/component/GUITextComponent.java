package com.caveexp.gui.component;

import com.caveexp.Main;
import com.caveexp.gui.font.Font;
import com.caveexp.util.Renderer;

public class GUITextComponent extends GUIComponent {
    public String text = "";
    public void render(Renderer renderer) {
        Font.render(renderer, absX, absY, text);
    }
    public static GUITextComponent build(String label, int x, int y) {
        GUITextComponent text = new GUITextComponent();
        text.x = x;
        text.y = y;
        text.text = label;
        return text;
    }
}
