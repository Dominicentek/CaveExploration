package com.caveexp.util;

import com.badlogic.gdx.Gdx;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.util.Properties;

public enum Input {
    UP(com.badlogic.gdx.Input.Keys.W, false, "Move Up"),
    LEFT(com.badlogic.gdx.Input.Keys.A, false, "Move Left"),
    DOWN(com.badlogic.gdx.Input.Keys.S, false, "Move Down"),
    RIGHT(com.badlogic.gdx.Input.Keys.D, false, "Move Right"),
    ACTION(com.badlogic.gdx.Input.Buttons.LEFT, true, "Action"),
    REMOVE_ITEM(com.badlogic.gdx.Input.Buttons.RIGHT, true, "Remove Item"),
    CRAFTING(com.badlogic.gdx.Input.Keys.E, false, "Open Inventory"),
    REGION_SELECTOR(com.badlogic.gdx.Input.Keys.Q, false, "Open Region Selector"),
    TRAVEL_TO_REGION(com.badlogic.gdx.Input.Keys.R, false, "Travel to Region"),
    CLOSE_MENU(com.badlogic.gdx.Input.Keys.ESCAPE, false, "Close Menu"),
    PAUSE(com.badlogic.gdx.Input.Keys.ENTER, false, "Pause Game"),
    FULLSCREEN(com.badlogic.gdx.Input.Keys.F11, false, "Fullscreen");
    public static final File file = new File("keys.txt");
    public static int mouseX = 0;
    public static int mouseY = 0;
    public static boolean mouseClicked = false;
    public static boolean isMousePressed = false;
    public static int scroll;
    public int key;
    public boolean button;
    public String name;
    Input(int key, boolean button, String name) {
        this.key = key;
        this.button = button;
        this.name = name;
    }
    public boolean isPressed() {
        return (!button && Gdx.input.isKeyPressed(key)) || (button && Gdx.input.isButtonPressed(key));
    }
    public int getKeyCode() {
        return key;
    }
    public boolean isJustPressed() {
        return (!button && Gdx.input.isKeyJustPressed(key)) || (button && Gdx.input.isButtonJustPressed(key));
    }
    public static void parse() {
        try {
            if (file.exists()) {
                Properties properties = new Properties();
                properties.load(new FileReader(file));
                for (Input input : values()) {
                    String key = input.name().toLowerCase();
                    if (properties.containsKey(key)) input.parseKey(properties.getProperty(key));
                }
            }
            save();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void parseKey(String key) {
        int keycode = Integer.parseInt(key.substring(0, key.length() - 1));
        this.key = keycode;
        char inputType = key.charAt(key.length() - 1);
        if (inputType == 'b') button = true;
        else if (inputType == 'k') button = false;
        else throw new RuntimeException("Invalid input type " + inputType);
    }
    public static void save() {
        try {
            Properties properties = new Properties();
            for (Input input : values()) {
                properties.put(input.name().toLowerCase(), input.key + (input.button ? "b" : "k"));
            }
            properties.store(new FileOutputStream(file), "Cave Exploration Keybinds File (delete to reset)");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String toString() {
        if (button) {
            if (key == 0) return "Left Click";
            else if (key == 1) return "Right Click";
            else if (key == 2) return "Middle Click";
            else if (key == 3) return "Backward Button";
            else if (key == 4) return "Forward Button";
            else throw new IllegalStateException("Invalid button: " + key);
        }
        return com.badlogic.gdx.Input.Keys.toString(key);
    }
}
