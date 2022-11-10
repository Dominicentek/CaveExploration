package com.caveexp.command;

public class AccessCounter {
    private int value;
    public AccessCounter(int value) {
        this.value = value;
    }
    public int access() {
        return value++;
    }
    public int get() {
        return value;
    }
}
