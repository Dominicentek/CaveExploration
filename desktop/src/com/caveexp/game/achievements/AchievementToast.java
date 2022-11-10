package com.caveexp.game.achievements;

public class AchievementToast {
    public int timeout = 300;
    public String name;
    public int yIndex = 0;
    public AchievementToast(int timeout, String name, int yIndex) {
        this.timeout = timeout;
        this.name = name;
        this.yIndex = yIndex;
    }
}
