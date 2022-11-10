package com.caveexp.game.achievements;

import com.badlogic.gdx.graphics.Texture;
import com.caveexp.util.Registry;
import com.caveexp.util.bjson.BJSONFile;
import com.caveexp.util.bjson.ObjectElement;
import java.io.File;

public class Achievement {
    public final String name;
    public final String description;
    private boolean achieved;
    private boolean hidden;
    public Achievement(String name, String description) {
        this.name = name;
        this.description = description;
    }
    public void grant() {
        achieved = true;
    }
    public void revoke() {
        achieved = false;
    }
    public boolean isAchieved() {
        return achieved;
    }
    public boolean isHidden() {
        return hidden;
    }
    public Achievement hidden() {
        hidden = true;
        return this;
    }
    public static void register() {
        Registry.ACHIEVEMENTS.register("craft_first_item", new Achievement("First Craft", "Craft an item"));
        Registry.ACHIEVEMENTS.register("kill_spider", new Achievement("Spider Infested Cave", "Kill a spider"));
        Registry.ACHIEVEMENTS.register("drink_potion", new Achievement("I'm a wizard!", "Drink a potion"));
        Registry.ACHIEVEMENTS.register("region_travel", new Achievement("Region Traveller", "Travel to a region"));
        Registry.ACHIEVEMENTS.register("dark", new Achievement("Dark!?", "Travel to 3rd region"));
        Registry.ACHIEVEMENTS.register("platinum", new Achievement("Is that platinum?", "Find platinum ore"));
        Registry.ACHIEVEMENTS.register("big_spider", new Achievement("That's a big spider!", "Dig through the hallway in 5th region"));
        Registry.ACHIEVEMENTS.register("did_i_win", new Achievement("Uhh, did I win?", "Yes you did"));
        Registry.ACHIEVEMENTS.register("game_over", new Achievement("\"Why I didn't use my potions earlier!\"", "Die to a spider"));
        Registry.ACHIEVEMENTS.register("100_potions", new Achievement("Congratulations?", "You just got 100 potions!").hidden());
        Registry.ACHIEVEMENTS.register("croc_pickaxe", new Achievement("What is that...?", "A croc pickaxe").hidden());
        Registry.ACHIEVEMENTS.register("how_did_you_find_this", new Achievement("How did you find this?", "How? Just how.").hidden());
        Registry.ACHIEVEMENTS.register("developer", new Achievement("Ah, a fellow developer!", "Greetings brother.").hidden());
        Registry.ACHIEVEMENTS.register("cheater", new Achievement("CHEATER!!1", "Use a cheat code or a command").hidden());
    }
}
