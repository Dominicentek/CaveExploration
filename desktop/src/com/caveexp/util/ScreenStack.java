package com.caveexp.util;

import com.caveexp.gui.screens.Screen;
import java.util.Iterator;
import java.util.Stack;

public class ScreenStack implements Iterable<Screen> {
    private final Stack<Screen> stack = new Stack<>();
    public void push(Screen screen) {
        if (!screen.isInitialized) {
            screen.init();
            screen.isInitialized = true;
        }
        stack.push(screen);
        screen.push();
    }
    public void pop() {
        stack.peek().pop();
        stack.pop();
    }
    public void popPush(Screen screen) {
        pop();
        push(screen);
    }
    public void clear() {
        stack.clear();
    }
    public Screen peek() {
        return stack.peek();
    }
    public Iterator<Screen> iterator() {
        return stack.iterator();
    }
    public ScreenStack copy() {
        ScreenStack stack = new ScreenStack();
        stack.stack.addAll(this.stack);
        return stack;
    }
    public int size() {
        return stack.size();
    }
}
