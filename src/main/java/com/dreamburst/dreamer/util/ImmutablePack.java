package com.dreamburst.dreamer.util;

import java.util.Iterator;

public class ImmutablePack<E> implements Iterable<E> {

    private final Pack<E> pack;

    public ImmutablePack(Pack<E> pack) {
        this.pack = pack;
    }

    public int size() {
        return pack.size();
    }

    public int getCapacity() {
        return pack.getCapacity();
    }

    public boolean isEmpty() {
        return pack.isEmpty();
    }

    public boolean isWithinBounds(int index) {
        return pack.isWithinBounds(index);
    }

    public E get(int index) {
        return pack.get(index);
    }

    public boolean contains(E element) {
        return pack.contains(element);
    }

    public Object[] toArray() {
        return pack.toArray();
    }

    public <T> T[] toArray(T[] array) {
        return pack.toArray(array);
    }

    public Iterator<E> iterator() {
        return pack.iterator();
    }
}
