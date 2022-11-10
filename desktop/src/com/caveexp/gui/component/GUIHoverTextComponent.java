package com.caveexp.gui.component;

import com.caveexp.util.Input;

public abstract class GUIHoverTextComponent extends GUIComponent {
    public String hoverText = "";
    public int width;
    public int height;
    public boolean isInBounds() {
        return Input.mouseX >= absX && Input.mouseY >= absY && Input.mouseX <= absX + width && Input.mouseY <= absY + height;
    }
}
