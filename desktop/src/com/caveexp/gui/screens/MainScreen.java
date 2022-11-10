package com.caveexp.gui.screens;

import com.caveexp.game.Game;
import com.caveexp.util.Renderer;

public class MainScreen extends Screen {
    public void update() {
        Game.update();
    }
    public void render(Renderer renderer) {
        Game.render(renderer);
    }
}
