package com.dreamburst.dreamer.util;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;

public class Pack<E> implements Iterable<E> {

    private transient E[] elements;
    private int size = 0;

    private PackIterator iterator;

    public Pack() {
        this(32);
    }

    public Pack(List<E> list) {
        this(list.size());

        for (int i = 0; i < list.size(); i ++) {
            set(i, list.get(i));
        }
    }

    @SuppressWarnings("unchecked")
    public Pack(int capacity) {
        elements = (E[]) new Object[capacity];
        iterator = new PackIterator();
    }

    public int size() {
        return size;
    }

    public int getCapacity() {
        return elements.length;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean isWithinBounds(int index) {
        return index < getCapacity();
    }

    public boolean contains(E element) {
        for (int i = 0; i < size; i ++) {
            if (element == elements[i]) {
                return true;
            }
        }

        return false;
    }

    @SuppressWarnings("unchecked")
    public Object[] toArray() {
        E[] copy = (E[]) new Object[size];
        System.arraycopy(elements, 0, copy, 0, Math.min(elements.length, size));
        return copy;
    }

    public <T> T[] toArray(T[] array) {
        T[] copy = (T[]) new Object[size];

        if (array.length < size) {
            if (array.getClass() != Object[].class) {
                copy = (T[]) Array.newInstance(array.getClass().getComponentType(), size);
            }
        }

        System.arraycopy(elements, 0, copy, 0, Math.min(elements.length, size));

        return copy;
    }

    public E get(int index) {
        return elements[index];
    }

    public void set(int index, E element)  {
        if (index >= elements.length) {
            grow(getGrowthCapacity(index));
        }

        size = index + 1 < size ? size : index + 1;
        elements[index] = element;
    }

    public void add(E element) {
        if (size == elements.length) {
            grow();
        }

        elements[size++] = element;
    }

    public boolean remove(E element) {
        for (int i = 0; i < size; i ++) {
            E existingElement = elements[i];

            if (element == existingElement) {
                elements[i] = elements[--size];
                elements[size] = null;
                return true;
            }
        }

        return false;
    }

    public E removeLast() {
        if (size > 0) {
            E element = elements[--size];
            elements[size] = null;
            return element;
        }

        return null;
    }

    public void clear() {
        for (int i = 0; i < size; i ++) {
            elements[i] = null;
        }

        size = 0;
    }

    private void grow() {
        grow(getGrowthCapacity(elements.length));
    }

    @SuppressWarnings("unchecked")
    private void grow(int capacity) {
        E[] snapshot = elements;
        elements = (E[]) new Object[capacity];
        System.arraycopy(snapshot, 0, elements, 0, snapshot.length);
    }

    private int getGrowthCapacity(int capacity) {
        return (capacity * 3) / 2 + 1;
    }

    public Iterator<E> iterator() {
        return iterator;
    }

    private class PackIterator implements Iterator<E> {

        private int index = 0;

        public boolean hasNext() {
            return index < size;
        }

        public E next() {
            E element = elements[index++];

            while (element == null) {
                element = elements[index++];
            }

            return element;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
