package com.caveexp.gui.screens;

import com.caveexp.Main;
import com.caveexp.game.Game;
import com.caveexp.game.inventory.Inventory;
import com.caveexp.game.inventory.InventoryItem;
import com.caveexp.game.item.Item;
import com.caveexp.gui.component.GUIImageTextButtonComponent;
import com.caveexp.gui.component.GUIImageTextComponent;
import com.caveexp.gui.component.GUITextComponent;
import com.caveexp.gui.font.Font;
import com.caveexp.util.Input;
import com.caveexp.util.Registry;
import com.caveexp.util.Renderer;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;

public class SandboxInventoryScreen extends CloseableScreen {
    private final ArrayList<Item> allItems = new ArrayList<>();
    private final Rectangle container = new Rectangle();
    private int rows = 0;
    public void init() {
        for (Item item : Registry.ITEMS) {
            if (item.hideFromSandboxInventory) continue;
            allItems.add(item);
        }
        container.width = 3 + Inventory.SLOTS * 37;
        rows = (int)Math.ceil((allItems.size() / (double)Inventory.SLOTS));
        container.height = (rows + 1) * 37 + 3 + 2;
        container.x = Main.windowWidth / 2 - container.width / 2;
        container.y = Main.windowHeight / 2 - container.height / 2;
    }
    public void push() {
        removeAllComponents();
        refreshInventoryButtons();
        for (int i = 0; i < allItems.size(); i++) {
            int x = i % Inventory.SLOTS;
            int y = i / Inventory.SLOTS;
            final int index = i;
            addComponent(GUIImageTextButtonComponent.build(container.x + 4 + x * 37, container.y + 4 + y * 37, allItems.get(i).texture, allItems.get(i).name, () -> {
                Game.inventory.addItem(new InventoryItem(Registry.ITEMS.getID(allItems.get(index))));
                refreshInventoryButtons();
            }));
        }
    }
    public void refreshInventoryButtons() {
        for (int i = 0; i < Inventory.SLOTS; i++) {
            final int index = i;
            if (Game.inventory.items[i].item.equals("air")) continue;
            Item item = Registry.ITEMS.get(Game.inventory.items[i].item);
            GUIImageTextButtonComponent component = GUIImageTextButtonComponent.build(container.x + 4 + i * 37, container.y + 6 + rows * 37, item.texture, item.name, null);
            GUITextComponent text = GUITextComponent.build(Game.inventory.items[i].count + "", container.x + 4 + i * 37 + 32 - Font.stringWidth(Game.inventory.items[i].count + "") - 1, container.y + 6 + rows * 37 + 32 + 2 - Font.getHeight());
            component.action = () -> {
                Game.inventory.putItem(new InventoryItem(), index);
                removeComponent(component);
                removeComponent(text);
            };
            addComponent(component);
            addComponent(text);
        }
    }
    public void render(Renderer renderer) {
        renderer.setColor(0x0000007F);
        renderer.rectfill(container.x, container.y, container.width, container.height);
        renderer.setColor(0xFFFFFFFF);
        renderer.rectdraw(container.x, container.y, container.width, container.height);
        for (int x = 0; x < Inventory.SLOTS; x++) {
            for (int y = 0; y < rows; y++) {
                int X = container.x + 2 + x * 37;
                int Y = container.y + 2 + y * 37;
                renderer.rectdraw(X, Y, 36, 36);
                if (Input.mouseX >= X + 2 && Input.mouseY >= Y + 2 && Input.mouseX <= X + 34 && Input.mouseY <= Y + 34) {
                    renderer.setColor(0xFFFFFF7F);
                    renderer.rectfill(X + 1, Y + 1, 34, 34);
                    renderer.setColor(0xFFFFFFFF);
                }
            }
        }
        renderer.rectfill(container.x + 2, container.y + 2 + rows * 37, container.width - 4, 1);
        for (int x = 0; x < Inventory.SLOTS; x++) {
            int X = container.x + 2 + x * 37;
            int Y = container.y + 4 + rows * 37;
            renderer.rectdraw(X, Y, 36, 36);
            if (Input.mouseX >= X + 2 && Input.mouseY >= Y + 2 && Input.mouseX <= X + 34 && Input.mouseY <= Y + 34) {
                renderer.setColor(0xFFFFFF7F);
                renderer.rectfill(X + 1, Y + 1, 34, 34);
                renderer.setColor(0xFFFFFFFF);
            }
        }
    }
    public void update() {
        if (Input.CRAFTING.isJustPressed()) Main.screenStack.pop();
    }
}
