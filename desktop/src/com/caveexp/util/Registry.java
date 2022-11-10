package com.caveexp.util;

import com.caveexp.game.SoundEvent;
import com.caveexp.game.achievements.Achievement;
import com.caveexp.game.item.Item;
import com.caveexp.game.ores.Ore;
import com.caveexp.game.recipes.Recipe;
import java.lang.reflect.Array;
import java.util.*;

public class Registry<T> implements Iterable<T> {
    public static final Registry<Ore> ORES = new Registry<>();
    public static final Registry<Item> ITEMS = new Registry<>();
    public static final Registry<Recipe> RECIPES = new Registry<>();
    public static final Registry<Achievement> ACHIEVEMENTS = new Registry<>();
    public static final Registry<SoundEvent> SOUNDS = new Registry<>();
    private final LinkedHashMap<String, T> byID = new LinkedHashMap<>();
    private final ArrayList<T> byNumber = new ArrayList<>();
    public T register(String id, T value) {
        byID.put(id, value);
        byNumber.add(value);
        return value;
    }
    public T get(String id) {
        return byID.get(id);
    }
    public T get(int id) {
        return byNumber.get(id);
    }
    public int getNumericID(T value) {
        return byNumber.indexOf(value);
    }
    public int getNumericID(String id) {
        return getNumericID(get(id));
    }
    public int amount() {
        return byNumber.size();
    }
    public String getID(T value) {
        for (String id : byID.keySet()) {
            if (byID.get(id) == value) return id;
        }
        return null;
    }
    public String getID(int id) {
        return getID(get(id));
    }
    public String[] ids() {
        return byID.keySet().toArray(new String[0]);
    }
    @SafeVarargs
    public final T[] array(T... dummy) {
        if (dummy.length > 0) throw new IllegalArgumentException("Do not provide any values for dummy parameter");
        T[] array = (T[])Array.newInstance(dummy.getClass().getComponentType(), amount());
        for (int i = 0; i < array.length; i++) {
            array[i] = byNumber.get(i);
        }
        return array;
    }
    public Iterator<T> iterator() {
        return Arrays.stream(array()).iterator();
    }
}
