package com.caveexp.util;

import java.lang.reflect.Array;

public class ArrayUtils {
    public static <T> T[] prepend(T value, T[] array) {
        T[] prepended = (T[])Array.newInstance(array.getClass().getComponentType(), array.length + 1);
        prepended[0] = value;
        for (int i = 0; i < array.length; i++) {
            prepended[i + 1] = array[i];
        }
        return prepended;
    }
    public static <T> T[] append(T[] array, T value) {
        T[] appended = (T[])Array.newInstance(array.getClass().getComponentType(), array.length + 1);
        for (int i = 0; i < array.length; i++) {
            appended[i] = array[i];
        }
        appended[array.length] = value;
        return appended;
    }
    public static String[] enumNames(Class<? extends Enum<?>> enumClass) {
        Enum<?>[] values = enumClass.getEnumConstants();
        String[] names = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            names[i] = values[i].name();
        }
        return names;
    }
}
