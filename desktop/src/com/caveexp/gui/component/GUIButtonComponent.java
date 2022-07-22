package com.caveexp.gui.component;

import com.caveexp.Main;
import com.caveexp.gui.font.Font;
import com.caveexp.util.Input;
import com.caveexp.util.Renderer;

public class GUIButtonComponent extends GUIComponent {
    public String text = "";
    public int width = 0;
    public int height = 0;
    public boolean locked = false;
    public Runnable action = () -> {};
    public void update() {
        if (isHovering(absX, absY, width, height) && Input.mouseClicked && !locked && Main.screenStack.peek() == parent) {
            action.run();
        }
    }
    public void render(Renderer renderer) {
        int color = locked ? 0xCFCFCFFF : 0x000000FF;
        if (isHovering(absX, absY, width, height) && !locked && Main.screenStack.peek() == parent) renderer.setColor(0xCFCFCFFF);
        else renderer.setColor(0xFFFFFFFF);
        renderer.rectfill(absX, absY, width, height);
        renderer.setColor(color);
        renderer.rectdraw(absX, absY, width, height);
        renderer.setColor(0xFFFFFFFF);
        Font.render(renderer, absX + width / 2 - Font.stringWidth(text) / 2, absY + height / 2 - Font.getHeight() / 2, (locked ? "$cCFCFCF" : "$c000000") + text);
    }
    public static GUIButtonComponent build(int x, int y, int width, int height, String text, Runnable action) {
        GUIButtonComponent button = new GUIButtonComponent();
        button.x = x;
        button.y = y;
        button.width = width;
        button.height = height;
        button.text = text;
        button.action = action;
        return button;
    }
}
