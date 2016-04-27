package com.dreamburst.dreamer.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public class ObservableList<E> extends ArrayList<E> {

    private View<E> view;

    public ObservableList() {
        view = new View<>(this);
    }

    public ImmutableList<E> filter(Predicate<E> predicate) {
        return view.get(predicate);
    }

    public void updateView() {
        view.update();
    }

    @Override
    public boolean add(E e) {
        boolean result =  super.add(e);

        updateView();

        return result;
    }

    @Override
    public E remove(int index) {
        E result =  super.remove(index);

        updateView();

        return result;
    }

    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean result =  super.addAll(c);

        updateView();

        return result;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result =  super.removeAll(c);

        updateView();

        return result;
    }
}
