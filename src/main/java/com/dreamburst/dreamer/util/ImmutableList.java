package com.dreamburst.dreamer.util;

import java.util.Iterator;
import java.util.List;

public class ImmutableList<E> implements Iterable<E> {

    private List<E> list;

    public ImmutableList(List<E> list) {
        this.list = list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }

    public int size() {
        return list.size();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public E get(int index) {
        return list.get(index);
    }

    public boolean contains(E element) {
        return list.contains(element);
    }

    public Object[] toArray() {
        return list.toArray();
    }

    public <T> T[] toArray(T[] array) {
        return list.toArray(array);
    }

    public Iterator<E> iterator() {
        return list.iterator();
    }
}
