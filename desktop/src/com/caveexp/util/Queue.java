package com.caveexp.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Queue<T> implements Iterable<T> {
    private final ArrayList<T> list = new ArrayList<>();
    public Queue() {}
    public Queue(Queue<T> toastQueue) {
        for (T value : toastQueue) {
            push(value);
        }
    }
    public void push(T value) {
        list.add(value);
    }
    public void pop() {
        list.remove(0);
    }
    public T peek() {
        return list.get(0);
    }
    public int size() {
        return list.size();
    }
    public T get(int index) {
        return list.get(index);
    }
    public Iterator<T> iterator() {
        Collections.reverse(list);
        Iterator<T> iterator = list.iterator();
        Collections.reverse(list);
        return iterator;
    }
}
