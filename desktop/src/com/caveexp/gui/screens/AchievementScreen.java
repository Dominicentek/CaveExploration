package com.caveexp.gui.screens;

import com.caveexp.Main;
import com.caveexp.game.achievements.Achievement;
import com.caveexp.gui.component.GUIButtonComponent;
import com.caveexp.gui.component.GUIComponent;
import com.caveexp.gui.component.PositionAnchor;
import com.caveexp.gui.font.Font;
import com.caveexp.util.Input;
import com.caveexp.util.Registry;
import com.caveexp.util.Renderer;

public class AchievementScreen extends Screen {
    private Screen screen;
    private boolean darken;
    public AchievementScreen(Screen screen, boolean darken) {
        this.screen = screen;
        this.darken = darken;
    }
    public void render(Renderer renderer) {
        if (darken) {
            renderer.setColor(0x0000007F);
            renderer.rectfill(0, 0, Main.windowWidth, Main.windowHeight);
            renderer.setColor(0xFFFFFFFF);
        }
        Font.render(renderer, Main.windowWidth / 2 - Font.stringWidth("Achievements") / 2, 25, "Achievements");
        int i = 0;
        for (Achievement achievement : Registry.ACHIEVEMENTS) {
            if (achievement.isHidden() && !achievement.isAchieved()) continue;
            renderer.setColor(achievement.isAchieved() ? 0xFFFFFFFF : 0xFFFFFF7F);
            renderer.rectfill(Main.windowWidth / 2 - 256, i * (10 + Font.getHeight() * 2 + 5) + 50, 512, 10 + Font.getHeight() * 2);
            renderer.setColor(0x000000FF);
            renderer.rectdraw(Main.windowWidth / 2 - 256, i * (10 + Font.getHeight() * 2 + 5) + 50, 512, 10 + Font.getHeight() * 2);
            renderer.setColor(0xFFFFFFFF);
            Font.render(renderer, Main.windowWidth / 2 - 256 + 5, i * (10 + Font.getHeight() * 2 + 5) + 55, "$n$b$c000000" + achievement.name);
            Font.render(renderer, Main.windowWidth / 2 - 256 + 5, i * (10 + Font.getHeight() * 2 + 5) + 55 + Font.getHeight(), "$n$i$c000000" + achievement.description);
            i++;
        }
    }
    public void init() {
        addComponent(GUIButtonComponent.build(-128, -37, 256, 32, "$nDone", () -> {
            Main.screenStack.popPush(screen);
        }).anchored(PositionAnchor.BOTTOM_CENTER));
    }
}
