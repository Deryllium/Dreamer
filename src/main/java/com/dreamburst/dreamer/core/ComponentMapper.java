package com.dreamburst.dreamer.core;

import com.dreamburst.dreamer.util.ImmutableList;

import java.util.*;
import java.util.stream.Collectors;

public class ComponentMapper {

    private final Map<Class<? extends Component>[], ImmutableList<Entity>> componentMap = new HashMap<>();

    public void update(List<Entity> entities) {
        for (Map.Entry<Class<? extends Component>[], ImmutableList<Entity>> entry : componentMap.entrySet()) {
            entry.getValue().setList(entities.stream().filter(entity -> entity.has(entry.getKey())).collect(Collectors.toList()));
        }
    }

    private void register(List<Entity> entities, Class<? extends Component>... components) {
        componentMap.put(components, new ImmutableList<>(entities.stream().filter(entity -> entity.has(components)).collect(Collectors.toList())));
    }

    public ImmutableList<Entity> getEntitiesFor(List<Entity> entities, Class<? extends Component>... components) {
        Arrays.sort(components, new ComonentComparator());
        if (!componentMap.containsKey(components)) {
            register(entities, components);
        }

        return componentMap.get(components);
    }

    private class ComonentComparator implements Comparator<Class<? extends Component>> {
        @Override
        public int compare(Class<? extends Component> one, Class<? extends Component> two) {
            return (int) Math.signum(ComponentType.getIndexFor(one) - ComponentType.getIndexFor(two));
        }
    }
}
