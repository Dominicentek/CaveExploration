package com.caveexp.gui.screens;

import com.caveexp.Main;
import com.caveexp.game.Game;
import com.caveexp.game.recipes.Recipe;
import com.caveexp.gui.component.*;
import com.caveexp.gui.font.Font;
import com.caveexp.util.Input;
import com.caveexp.util.Registry;
import com.caveexp.util.Renderer;

public class CraftingScreen extends CloseableScreen {
    public void init() {
        int width = 365;
        int height = 338;
        int X = -width / 2;
        int Y = -height / 2;
        for (int i = 0; i < Registry.RECIPES.amount(); i++) {
            Recipe recipe = Registry.RECIPES.get(i);
            int x = i / 9;
            int y = i % 9;
            GUIButtonComponent craft = GUIButtonComponent.build(5 + x * 180 + X, 5 + y * 37 + Y, 64, 32, "Craft", null);
            craft.action = () -> {
                Game.inventory.craft(recipe);
                int j = 0;
                for (GUIComponent component : getComponents()) {
                    if (component instanceof GUIButtonComponent) {
                        ((GUIButtonComponent)component).locked = !Registry.RECIPES.get(j).canCraft(Game.inventory);
                        j++;
                    }
                }
            };
            craft.locked = !recipe.canCraft(Game.inventory);
            GUIImageTextComponent item1 = GUIImageTextComponent.build(74 + x * 180 + X, 5 + y * 37 + Y, Registry.ITEMS.get(recipe.item1.item).texture, "Input 1: " + Registry.ITEMS.get(recipe.item1.item).name);
            GUIImageTextComponent item2 = GUIImageTextComponent.build(74 + x * 180 + 37 + X, 5 + y * 37 + Y, Registry.ITEMS.get(recipe.item2.item).texture, "Input 2: " + Registry.ITEMS.get(recipe.item2.item).name);
            GUIImageTextComponent out = GUIImageTextComponent.build(74 + x * 180 + 37 * 2 + X, 5 + y * 37 + Y, Registry.ITEMS.get(recipe.result.item).texture, "Output: " + Registry.ITEMS.get(recipe.result.item).name);
            addComponent(craft.anchored(PositionAnchor.CENTER));
            addComponent(item1.anchored(PositionAnchor.CENTER));
            addComponent(item2.anchored(PositionAnchor.CENTER));
            addComponent(out.anchored(PositionAnchor.CENTER));
        }
        for (int i = 0; i < Registry.RECIPES.amount(); i++) {
            Recipe recipe = Registry.RECIPES.get(i);
            int x = i / 9;
            int y = i % 9;
            int item1 = recipe.item1.count;
            int item2 = recipe.item2.count;
            int item = recipe.result.count;
            addComponent(GUITextComponent.build("" + item1, 74 + x * 180 + X + 32 - Font.stringWidth("" + item1), 10 + y * 37 + Y + 32 - Font.getHeight()).anchored(PositionAnchor.CENTER));
            addComponent(GUITextComponent.build("" + item2, 74 + x * 180 + X + 32 + 37 - Font.stringWidth("" + item2), 10 + y * 37 + Y + 32 - Font.getHeight()).anchored(PositionAnchor.CENTER));
            addComponent(GUITextComponent.build("" + item, 74 + x * 180 + X + 32 + 37 * 2 - Font.stringWidth("" + item), 10 + y * 37 + Y + 32 - Font.getHeight()).anchored(PositionAnchor.CENTER));
        }
    }
    public void push() {
        if (Game.sandboxMode) Main.screenStack.popPush(new SandboxInventoryScreen());
    }
    public void update() {
        if (Input.CRAFTING.isJustPressed()) Main.screenStack.pop();
    }
    public void render(Renderer renderer) {
        int width = 365;
        int height = 338;
        int x = Main.windowWidth / 2 - width / 2;
        int y = Main.windowHeight / 2 - height / 2;
        renderer.setColor(0x0000007F);
        renderer.rectfill(x, y, width, height);
        renderer.setColor(0xFFFFFFFF);
        renderer.rectdraw(x, y, width, height);
    }
}
