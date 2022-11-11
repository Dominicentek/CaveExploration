package com.caveexp.game.toasts;

public abstract class Toast {
    public int timeout = 300;
    public String title;
    public String message;
    public int color;
    public int yIndex;
    public Toast(String title, String message, int color) {
        this.title = title;
        this.message = message;
        this.color = color;
    }
}
