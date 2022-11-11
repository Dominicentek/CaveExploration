package com.caveexp.game.toasts;

public class ErrorToast extends Toast {
    public ErrorToast() {
        super("An internal error occurred", "Please check the console", 0x7F0000);
    }
}
