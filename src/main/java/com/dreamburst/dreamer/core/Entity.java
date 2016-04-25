package com.dreamburst.dreamer.core;

import com.dreamburst.dreamer.core.events.ComponentEvent;
import com.dreamburst.dreamer.core.events.PreComponentEvent;
import com.dreamburst.dreamer.util.ImmutablePack;
import com.dreamburst.dreamer.util.Pack;

public class Entity extends EngineElement<Entity> {

    private Pack<Component> components;
    private ImmutablePack<Component> immutableComponents;

    private PreComponentEvent componentAdding;
    private PreComponentEvent componentRemoving;

    private ComponentEvent componentAdded;
    private ComponentEvent componentRemoved;

    public Entity() {
        components = new Pack<>();
        immutableComponents = new ImmutablePack<>(components);

        componentAdding = new PreComponentEvent(this);
        componentRemoving = new PreComponentEvent(this);

        componentAdded = new ComponentEvent(this);
        componentRemoved = new ComponentEvent(this);
    }

    /**
     * Gets the {@link Pack} of {@link Component}s belonging to this {@link Entity}. An {@link ImmutablePack} is
     * used for iterating purposes.
     *
     * @return  and {@link ImmutablePack} containing all of this {@link Entity}'s {@link Component}s
     */
    public ImmutablePack<Component> getComponents() {
        return immutableComponents;
    }

    /**
     * Gets the {@link Component} of the specified class type if this {@link Entity} contains it.
     *
     * @param type  the type of the {@link Component} to get
     * @param <T>   the type of the {@link Component}
     * @return      the {@link Component} of the same type specified if contained within this {@link Entity}, otherwise
     *              <code>null</code>
     */
    public <T extends Component> T get(Class<T> type) {
        return getComponent(ComponentType.getFor(type));
    }

    public boolean has(Class<? extends Component> type) {
        return get(type) != null;
    }

    public boolean has(Class<? extends Component>... types) {
        boolean hasAll = true;

        for (Class<? extends Component> type : types) {
            if (!has(type)) {
                hasAll = false;
            }
        }

        return hasAll;
    }

    /**
     * Adds the {@link Component} to this {@link Entity}
     *
     * @param component  the {@link Component} to add
     * @return           this {@link Entity} for chaining
     */
    public Entity add(Component component) {
        onComponentAdding().setComponent(component);
        onComponentAdding().execute();

        if (!onComponentAdding().isCancelled()) {
            addComponent(component);

            onComponentAdding().setComponent(null);
        }

        return this;
    }

    /**
     * Removes the {@link Component} with the specified {@link Class} type from this {@link Entity}
     *
     * @param type  the type of the {@link Component} to remove
     * @return      the removed {@link Component}, or <code>null</code> if this {@link Entity} did not contain a
     *              {@link Component} of the specified type.
     */
    public Component remove(Class<? extends Component> type) {
        ComponentType componentType = ComponentType.getFor(type);
        int componentTypeIndex = componentType.getIndex();
        Component component = components.get(componentTypeIndex);

        onComponentAdding().setComponent(component);
        onComponentAdding().execute();

        if (!onComponentAdding().isCancelled()) {
            removeComponent(type);

            onComponentAdding().setComponent(null);
        }

        return component;
    }

    public void clear() {
        while (components.size() > 0) {
            remove(components.get(0).getClass());
        }
    }

    public PreComponentEvent onComponentAdding() {
        return componentAdding;
    }

    public ComponentEvent onComponentAdded() {
        return componentAdded;
    }

    public PreComponentEvent onComponentRemoving() {
        return componentRemoving;
    }

    public ComponentEvent onComponentRemoved() {
        return componentRemoved;
    }

    private <T extends Component> T getComponent(ComponentType componentType) {
        int componentTypeIndex = componentType.getIndex();

        return componentTypeIndex < components.size()
                ? (T) components.get(componentTypeIndex)
                : null;
    }

    private boolean addComponent(Component component) {

        Class<? extends Component> type = component.getClass();
        Component currentComponent = get(type);

        if (component == currentComponent) {
            return false;
        }

        if (currentComponent != null) {
            removeComponent(type);
        }

        int componentTypeIndex = ComponentType.getIndexFor(type);
        components.set(componentTypeIndex, component);

        componentAdded.setComponent(component);
        componentAdded.execute();
        componentAdded.setComponent(null);

        return true;
    }

    private boolean removeComponent(Class<? extends Component> type) {
        ComponentType componentType = ComponentType.getFor(type);
        int componentTypeIndex = componentType.getIndex();
        Component component = components.get(componentTypeIndex);

        if (component != null) {
            components.set(componentTypeIndex, null);

            componentRemoved.setComponent(component);
            componentRemoved.execute();
            componentRemoved.setComponent(null);

            return true;
        }

        return false;
    }
}
