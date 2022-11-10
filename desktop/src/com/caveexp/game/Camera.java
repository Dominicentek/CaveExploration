package com.caveexp.game;

import com.caveexp.Main;

public class Camera {
    public static double x;
    public static double y;
    public static double targetX;
    public static double targetY;
    public static double windowX;
    public static double windowY;
    public static void update() {
        x += (targetX - x) / 25;
        y += (targetY - y) / 25;
        windowX = x - Main.windowWidth / 2.0;
        windowY = y - Main.windowHeight / 2.0;
    }
    public static void setTarget(double x, double y) {
        targetX = x;
        targetY = y;
    }
    public static void snap() {
        x = targetX;
        y = targetY;
    }
}
