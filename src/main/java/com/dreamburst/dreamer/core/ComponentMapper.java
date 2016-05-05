package com.dreamburst.dreamer.core;

import java.util.*;
import java.util.stream.Collectors;

final class ComponentMapper {

    private static final ComponentComparator componentComparator = new ComponentComparator();

    private final List<Entity> list;
    private final Map<Set<Class<? extends Component>>, ImmutableList<Entity>> map;

    ComponentMapper(List<Entity> list) {
        this.list = list;

        map = new HashMap<>();
    }

    @SafeVarargs
    final ImmutableList<Entity> get(Class<? extends Component>... types) {
        Arrays.sort(types, componentComparator);

        Set<Class<? extends Component>> typeSet = new LinkedHashSet<>(Arrays.asList(types));

        if (map.containsKey(typeSet)) {
            List<Entity> filtered = list.stream().filter(entity -> entity.has(types)).collect(Collectors.toList());
            List<Entity> current = map.get(typeSet).getList();

            if (list.stream().filter(entity -> entity.has(types)).collect(Collectors.toList()).equals(filtered)) {
                if (!filtered.equals(current)) {
                    current.clear();
                    current.addAll(filtered);
                }

                return map.get(typeSet);
            }
        }

        return observe(typeSet);
    }

    void update() {
        for (Map.Entry<Set<Class<? extends Component>>, ImmutableList<Entity>> entry : map.entrySet()) {
            List<Entity> filtered = list.stream().filter(entity -> entity.has(entry.getKey())).collect(Collectors.toList());
            List<Entity> current = entry.getValue().getList();

            if (!filtered.equals(current)) {
                current.clear();
                current.addAll(filtered);
            }
        }
    }

    private ImmutableList<Entity> observe(Set<Class<? extends Component>> components) {
        ImmutableList<Entity> result = new ImmutableList<>(list.stream().filter(entity -> entity.has(components)).collect(Collectors.toList()));

        map.put(components, result);

        return result;
    }

    private static class ComponentComparator implements Comparator<Class<? extends Component>> {
        @Override
        public int compare(Class<? extends Component> one, Class<? extends Component> two) {
            return (int) Math.signum(ComponentType.getIndexFor(one) - ComponentType.getIndexFor(two));
        }
    }
}
