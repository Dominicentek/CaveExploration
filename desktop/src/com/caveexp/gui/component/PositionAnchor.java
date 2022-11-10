package com.caveexp.gui.component;

public enum PositionAnchor {
    TOP_LEFT(0, 0),
    TOP_CENTER(0.5, 0),
    TOP_RIGHT(1, 0),
    CENTER_LEFT(0, 0.5),
    CENTER(0.5, 0.5),
    CENTER_RIGHT(1, 0.5),
    BOTTOM_LEFT(0, 1),
    BOTTOM_CENTER(0.5, 1),
    BOTTOM_RIGHT(1, 1);
    public final double anchorX;
    public final double anchorY;
    PositionAnchor(double anchorX, double anchorY) {
        this.anchorX = anchorX;
        this.anchorY = anchorY;
    }
}
