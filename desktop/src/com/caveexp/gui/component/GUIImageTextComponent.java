package com.caveexp.gui.component;

import com.badlogic.gdx.graphics.Texture;
import com.caveexp.Main;
import com.caveexp.util.Renderer;

public class GUIImageTextComponent extends GUIHoverTextComponent {
    public Texture image;
    public void render(Renderer renderer) {
        renderer.draw(image, absX, absY);
    }
    public void update() {
        width = image.getWidth();
        height = image.getHeight();
    }
    public static GUIImageTextComponent build(int x, int y, Texture image, String label) {
        GUIImageTextComponent component = new GUIImageTextComponent();
        component.x = x;
        component.y = y;
        component.image = image;
        component.hoverText = label;
        return component;
    }
}
