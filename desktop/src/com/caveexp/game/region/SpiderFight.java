package com.caveexp.game.region;

import com.caveexp.game.Game;
import com.caveexp.game.SoundEvent;
import com.caveexp.game.item.Item;
import com.caveexp.game.item.Sword;
import com.caveexp.game.item.Tool;
import com.caveexp.util.RNG;
import com.caveexp.util.Registry;

public class SpiderFight {
    public int spiderHealth;
    public int spiderStrength;
    public int x;
    public int y;
    public final int maxHealth;
    public SpiderFight(int x, int y, int spiderHealth, int spiderStrength) {
        this.x = x;
        this.y = y;
        this.spiderStrength = spiderStrength;
        this.spiderHealth = spiderHealth;
        this.maxHealth = spiderHealth;
    }
    public boolean attack(Item item) {
        Registry.SOUNDS.get("damage").play();
        int strength = 1;
        if (item instanceof Sword) strength = ((Sword)item).damage;
        spiderHealth -= strength;
        if (spiderHealth <= 0) {
            Game.grantAchievement(Registry.ACHIEVEMENTS.get("kill_spider"));
            if (Registry.ACHIEVEMENTS.get("did_i_win").isAchieved()) Game.grantAchievement(Registry.ACHIEVEMENTS.get("kept_playing"));
            return true;
        }
        if (RNG.chance(3)) Game.health -= spiderStrength;
        if (item instanceof Sword) ((Tool)item).damage();
        return false;
    }
}
