package com.caveexp.util;

import java.util.*;

public class FinalList<E> implements List<E> {
    private final ArrayList<E> list = new ArrayList<>();
    @SafeVarargs
    public FinalList(E... elements) {
        this(Arrays.asList(elements));
    }
    public FinalList(Collection<E> list) {
        this.list.addAll(list);
    }
    public int size() {
        return list.size();
    }
    public boolean isEmpty() {
        return list.isEmpty();
    }
    public boolean contains(Object o) {
        return list.contains(o);
    }
    public Iterator<E> iterator() {
        return list.iterator();
    }
    public Object[] toArray() {
        return new Object[0];
    }
    public boolean add(Object o) {
        throw new IllegalStateException("This list is final and cannot be modified");
    }
    public boolean remove(Object o) {
        throw new IllegalStateException("This list is final and cannot be modified");
    }
    public boolean addAll(Collection c) {
        throw new IllegalStateException("This list is final and cannot be modified");
    }
    public boolean addAll(int index, Collection c) {
        throw new IllegalStateException("This list is final and cannot be modified");
    }
    public void clear() {
        throw new IllegalStateException("This list is final and cannot be modified");
    }
    public boolean equals(Object o) {
        return list.equals(o);
    }
    public int hashCode() {
        return list.hashCode();
    }
    public E get(int index) {
        return list.get(index);
    }
    public E set(int index, Object element) {
        throw new IllegalStateException("This list is final and cannot be modified");
    }
    public void add(int index, Object element) {
        throw new IllegalStateException("This list is final and cannot be modified");
    }
    public E remove(int index) {
        throw new IllegalStateException("This list is final and cannot be modified");
    }
    public int indexOf(Object o) {
        return list.indexOf(o);
    }
    public int lastIndexOf(Object o) {
        return list.lastIndexOf(o);
    }
    public ListIterator<E> listIterator() {
        return list.listIterator();
    }
    public ListIterator<E> listIterator(int index) {
        return list.listIterator(index);
    }
    public List<E> subList(int fromIndex, int toIndex) {
        return list.subList(fromIndex, toIndex);
    }
    public boolean retainAll(Collection c) {
        throw new IllegalStateException("This list is final and cannot be modified");
    }
    public boolean removeAll(Collection c) {
        throw new IllegalStateException("This list is final and cannot be modified");
    }
    public boolean containsAll(Collection c) {
        return list.containsAll(c);
    }
    public Object[] toArray(Object[] a) {
        return list.toArray(a);
    }
}
