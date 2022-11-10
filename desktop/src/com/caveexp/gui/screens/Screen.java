package com.caveexp.gui.screens;

import com.caveexp.gui.component.GUIComponent;
import com.caveexp.util.Renderer;

import java.util.ArrayList;

public abstract class Screen {
    public boolean isInitialized = false;
    private ArrayList<GUIComponent> components = new ArrayList<>();
    public void init() {}
    public void update() {}
    public void push() {}
    public void pop() {}
    public void keyPress(int key) {}
    public void key(char character) {}
    public void buttonPress(int button) {}
    public void render(Renderer renderer) {}
    public int getBackgroundColor() {
        return 0x000000FF;
    }
    public final GUIComponent[] getComponents() {
        GUIComponent[] components = new GUIComponent[this.components.size()];
        for (int i = 0; i < components.length; i++) {
            components[i] = this.components.get(i);
        }
        return components;
    }
    public final void addComponent(GUIComponent component) {
        component.parent = this;
        components.add(component);
    }
    public final void removeAllComponents() {
        components.clear();
    }
    public final void removeComponent(GUIComponent component) {
        components.remove(component);
    }
}
