package com.dreamburst.dreamer.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class View<E> {

    private final List<E> list;
    private final Map<Predicate<E>, ImmutableList<E>> views;

    View(List<E> list) {
        this.list = list;

        views = new HashMap<>();
    }

    ImmutableList<E> get(Predicate<E> predicate) {
        for (Map.Entry<Predicate<E>, ImmutableList<E>> entry : views.entrySet()) {
            List<E> filtered = list.stream().filter(predicate).collect(Collectors.toList());
            List<E> current = list.stream().filter(entry.getKey()).collect(Collectors.toList());

            if (filtered.equals(current)) {
                if (!current.equals(entry.getValue())) {
                    entry.getValue().getList().clear();
                    entry.getValue().getList().addAll(current);
                }

                return entry.getValue();
            }
        }

        observe(predicate);

        return views.get(predicate);
    }

    void update() {
        for (Map.Entry<Predicate<E>, ImmutableList<E>> entry : views.entrySet()) {
            List<E> current = list.stream().filter(entry.getKey()).collect(Collectors.toList());

            if (!current.equals(entry.getValue())) {
                entry.getValue().getList().clear();
                entry.getValue().getList().addAll(current);
            }
        }
    }

    private void observe(Predicate<E> predicate) {
        views.put(predicate, new ImmutableList<E>(list.stream().filter(predicate).collect(Collectors.toList())));
    }
}
