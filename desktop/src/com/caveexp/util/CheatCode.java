package com.caveexp.util;

import com.caveexp.game.Game;

public class CheatCode {
    private String code = "";
    private int progress;
    private final Runnable runnable;
    private long time = System.currentTimeMillis();
    public CheatCode(String code, Runnable runnable) {
        this.code = code;
        this.runnable = runnable;
    }
    public void inputChar(char character) {
        time = System.currentTimeMillis();
        if (code.charAt(progress) == character) {
            progress++;
            if (progress == code.length()) {
                progress = 0;
                runnable.run();
                Game.grantAchievement(Registry.ACHIEVEMENTS.get("cheater"));
            }
        }
        else progress = 0;
    }
    public void update() {
        long time = System.currentTimeMillis();
        if (time - this.time >= 1000) progress = 0;
    }
}