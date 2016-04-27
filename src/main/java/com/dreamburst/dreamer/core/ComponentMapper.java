package com.dreamburst.dreamer.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

final class ComponentMapper {

    private final List<Entity> list;
    private final Map<Class<? extends Component>[], ImmutableList<Entity>> views;

    ComponentMapper(List<Entity> list) {
        this.list = list;

        views = new HashMap<>();
    }

    @SafeVarargs
    final ImmutableList<Entity> get(Class<? extends Component>... components) {
        if (views.containsKey(components)) {
            List<Entity> filtered = list.stream().filter(entity -> entity.has(components)).collect(Collectors.toList());
            List<Entity> current = views.get(components).getList();

            if (list.stream().filter(entity -> entity.has(components)).collect(Collectors.toList()).equals(filtered)) {
                if (!filtered.equals(current)) {
                    current.clear();
                    current.addAll(filtered);
                }

                return views.get(components);
            }
        }

        return observe(components);
    }

    void update() {
        for (Map.Entry<Class<? extends Component>[], ImmutableList<Entity>> entry : views.entrySet()) {
            List<Entity> filtered = list.stream().filter(entity -> entity.has(entry.getKey())).collect(Collectors.toList());
            List<Entity> current = entry.getValue().getList();

            if (!filtered.equals(current)) {
                current.clear();
                current.addAll(filtered);
            }
        }
    }

    @SafeVarargs
    private final ImmutableList<Entity> observe(Class<? extends Component>... components) {
        ImmutableList<Entity> result = new ImmutableList<>(list.stream().filter(entity -> entity.has(components)).collect(Collectors.toList()));

        views.put(components, result);

        return result;
    }
}
