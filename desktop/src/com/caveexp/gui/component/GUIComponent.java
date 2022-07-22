package com.caveexp.gui.component;

import com.caveexp.Main;
import com.caveexp.gui.screens.Screen;
import com.caveexp.util.Input;
import com.caveexp.util.Renderer;

public abstract class GUIComponent {
    public int absX = -1000;
    public int absY = -1000;
    public int x = 0;
    public int y = 0;
    public PositionAnchor anchor = PositionAnchor.TOP_LEFT;
    public Screen parent = null;
    public void update() {}
    public abstract void render(Renderer renderer);
    public final boolean isHovering(int x, int y, int width, int height) {
        return Input.mouseX >= x && Input.mouseY >= y && Input.mouseX <= x + width && Input.mouseY <= y + height;
    }
    public final GUIComponent anchored(PositionAnchor anchor) {
        this.anchor = anchor;
        return this;
    }
    public final void updatePos() {
        absX = (int)(Main.windowWidth * anchor.anchorX) + x;
        absY = (int)(Main.windowHeight * anchor.anchorY) + y;
    }
}