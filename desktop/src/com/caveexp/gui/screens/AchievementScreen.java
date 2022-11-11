package com.caveexp.gui.screens;

import com.caveexp.Main;
import com.caveexp.game.Achievement;
import com.caveexp.gui.component.GUIButtonComponent;
import com.caveexp.gui.component.PositionAnchor;
import com.caveexp.gui.font.Font;
import com.caveexp.util.Registry;
import com.caveexp.util.Renderer;

public class AchievementScreen extends Screen {
    private Screen screen;
    public AchievementScreen(Screen screen) {
        this.screen = screen;
    }
    public void render(Renderer renderer) {
        renderer.setColor(0x0000007F);
        renderer.rectfill(0, 0, Main.windowWidth, Main.windowHeight);
        renderer.setColor(0xFFFFFFFF);
        Font.render(renderer, Main.windowWidth / 2 - Font.stringWidth("Achievements") / 2, 25, "Achievements");
        int i = 0;
        for (Achievement achievement : Registry.ACHIEVEMENTS) {
            if (achievement.isHidden() && !achievement.isAchieved()) continue;
            renderer.setColor(achievement.isAchieved() ? 0x00000000 : 0xFFFFFF3F);
            renderer.rectfill(Main.windowWidth / 2 - 256, i * (10 + Font.getHeight() * 2 + 5) + 50, 512, 10 + Font.getHeight() * 2);
            renderer.setColor(0xFFFFFFFF);
            renderer.rectdraw(Main.windowWidth / 2 - 256, i * (10 + Font.getHeight() * 2 + 5) + 50, 512, 10 + Font.getHeight() * 2);
            Font.render(renderer, Main.windowWidth / 2 - 256 + 5, i * (10 + Font.getHeight() * 2 + 5) + 55, "$b" + achievement.name);
            Font.render(renderer, Main.windowWidth / 2 - 256 + 5, i * (10 + Font.getHeight() * 2 + 5) + 55 + Font.getHeight(), "$i" + achievement.description);
            i++;
        }
    }
    public void init() {
        addComponent(GUIButtonComponent.build(-128, -37, 256, 32, "Done", () -> {
            Main.screenStack.popPush(screen);
        }).anchored(PositionAnchor.BOTTOM_CENTER));
    }
}
