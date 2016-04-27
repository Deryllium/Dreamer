package com.dreamburst.dreamer.core;

import java.util.ArrayList;
import java.util.Collection;

final class ObservableList extends ArrayList<Entity> {

    private ComponentMapper componentMapper;

    ObservableList() {
        componentMapper = new ComponentMapper(this);
    }

    @SafeVarargs
    final ImmutableList<Entity> filter(Class<? extends Component>... components) {
        return componentMapper.get(components);
    }

    void updateView() {
        componentMapper.update();
    }

    @Override
    public boolean add(Entity e) {
        boolean result =  super.add(e);

        updateView();

        return result;
    }

    @Override
    public Entity remove(int index) {
        Entity result =  super.remove(index);

        updateView();

        return result;
    }

    @Override
    public boolean addAll(Collection<? extends Entity> c) {
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
