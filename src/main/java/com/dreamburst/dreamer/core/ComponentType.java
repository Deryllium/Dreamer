package com.dreamburst.dreamer.core;

import java.util.HashMap;
import java.util.Map;

public final class ComponentType {

    private static final Map<Class<? extends Component>, ComponentType> types = new HashMap<>();
    private static final Map<Integer, Class<? extends Component>> indeces = new HashMap<>();

    private static int typeIndex = 0;

    private final int index;
    private final Class<? extends Component> type;

    private ComponentType(Class<? extends Component> type) {
        this.type = type;
        index = typeIndex++;
    }

    public int getIndex() {
        return index;
    }

    public Class<? extends Component> getType() {
        return type;
    }

    public static ComponentType getFor(Class<? extends Component> type) {
        ComponentType componentType = types.get(type);

        if (componentType == null) {
            componentType = new ComponentType(type);
            types.put(type, componentType);
            indeces.put(componentType.getIndex(), type);
        }

        return componentType;
    }

    public static int getIndexFor(Class<? extends Component> type) {
        return getFor(type).getIndex();
    }

    public static Class<? extends Component> getComponentTypeFor(int index) {
        return indeces.containsKey(index) ? indeces.get(index) : null;
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        ComponentType componentType = (ComponentType) obj;

        return index == componentType.index;
    }
}
