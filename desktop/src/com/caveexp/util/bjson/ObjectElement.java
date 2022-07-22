package com.caveexp.util.bjson;

import java.util.HashMap;
import java.util.Set;

public class ObjectElement {
    private HashMap<String, Object> elements = new HashMap<>();
    public void setObject(String name, ObjectElement value) {
        setObj(name, value);
    }
    public void setList(String name, ListElement value) {
        setObj(name, value);
    }
    public void setByte(String name, byte value) {
        setObj(name, value);
    }
    public void setShort(String name, short value) {
        setObj(name, value);
    }
    public void setInt(String name, int value) {
        setObj(name, value);
    }
    public void setLong(String name, long value) {
        setObj(name, value);
    }
    public void setFloat(String name, float value) {
        setObj(name, value);
    }
    public void setDouble(String name, double value) {
        setObj(name, value);
    }
    public void setString(String name, String value) {
        setObj(name, value);
    }
    public void setBoolean(String name, boolean value) {
        setObj(name, value);
    }
    public int size() {
        return elements.size();
    }
    public Set<String> keySet() {
        return elements.keySet();
    }
    public boolean contains(String name) {
        return elements.containsKey(name);
    }
    public boolean isObject(String name) {
        return isInstance(name, ObjectElement.class);
    }
    public boolean isList(String name) {
        return isInstance(name, ListElement.class);
    }
    public boolean isByte(String name) {
        return isInstance(name, Byte.class);
    }
    public boolean isShort(String name) {
        return isInstance(name, Short.class);
    }
    public boolean isInt(String name) {
        return isInstance(name, Integer.class);
    }
    public boolean isLong(String name) {
        return isInstance(name, Long.class);
    }
    public boolean isFloat(String name) {
        return isInstance(name, Float.class);
    }
    public boolean isDouble(String name) {
        return isInstance(name, Double.class);
    }
    public boolean isString(String name) {
        return isInstance(name, String.class);
    }
    public boolean isBoolean(String name) {
        return isInstance(name, Boolean.class);
    }
    public ObjectElement getObject(String name) {
        return get(name, ObjectElement.class, "Not an object");
    }
    public ListElement getList(String name) {
        return get(name, ListElement.class, "Not a list");
    }
    public byte getByte(String name) {
        return get(name, Byte.class, "Not a byte");
    }
    public short getShort(String name) {
        return get(name, Short.class, "Not a short");
    }
    public int getInt(String name) {
        return get(name, Integer.class, "Not an int");
    }
    public long getLong(String name) {
        return get(name, Long.class, "Not a long");
    }
    public float getFloat(String name) {
        return get(name, Float.class, "Not a float");
    }
    public double getDouble(String name) {
        return get(name, Double.class, "Not a double");
    }
    public String getString(String name) {
        return get(name, String.class, "Not a string");
    }
    public boolean getBoolean(String name) {
        return get(name, Boolean.class, "Not a boolean");
    }
    public void remove(String name) {
        if (!contains(name)) throw new BJSONException("Element " + name + " doesn't exist");
        elements.remove(name);
    }
    private <T> T get(String name, Class<T> clazz, String errorMsg) {
        Object obj = getObj(name);
        if (obj == null) throw new BJSONException("Element " + name + " doesn't exist");
        if (!isInstance(name, clazz)) throw new BJSONException(errorMsg);
        return clazz.cast(obj);
    }
    private void setObj(String name, Object obj) {
        elements.put(name, obj);
    }
    private Object getObj(String name) {
        return elements.get(name);
    }
    private boolean isInstance(String name, Class<?> clazz) {
        Object obj = getObj(name);
        if (obj == null) return false;
        return clazz.isInstance(obj);
    }
    public boolean equals(Object obj) {
        if (obj instanceof ObjectElement) {
            ObjectElement element = (ObjectElement)obj;
            if (element.size() == size()) {
                for (String name : element.keySet()) {
                    if (contains(name)) {
                        if (!elements.get(name).equals(element.elements.get(name))) return false;
                    }
                    else return false;
                }
                return true;
            }
        }
        return false;
    }
}
