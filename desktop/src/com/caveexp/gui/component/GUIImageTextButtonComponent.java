package com.caveexp.gui.component;

import com.badlogic.gdx.graphics.Texture;
import com.caveexp.util.Input;

public class GUIImageTextButtonComponent extends GUIImageTextComponent {
    public Runnable action = () -> {};
    public void update() {
        super.update();
        if (isHovering(absX, absY, width, height) && Input.mouseClicked) action.run();
    }
    public static GUIImageTextButtonComponent build(int x, int y, Texture texture, String label, Runnable action) {
        GUIImageTextButtonComponent component = new GUIImageTextButtonComponent();
        component.x = x;
        component.y = y;
        component.image = texture;
        component.hoverText = label;
        component.action = action;
        return component;
    }
}
