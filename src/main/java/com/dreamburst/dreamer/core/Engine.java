package com.dreamburst.dreamer.core;

import com.dreamburst.dreamer.delegate.Event;
import com.dreamburst.dreamer.delegate.EventExecutor;

import java.util.ArrayList;
import java.util.List;

public class Engine {

    private boolean enabled;
    private boolean updating;

    private ObservableList entities;
    private List<EntitySystem> systems;

    private final EventExecutor<Event> updateComponentObserver;

    public Engine() {
        entities = new ObservableList();
        systems = new ArrayList<>();

        updateComponentObserver = event -> entities.updateView();

        enabled = true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    /**
     * Adds an {@link EngineElement} to this {@link Engine}.
     *
     * @param element  the {@link EngineElement} to add
     * @param <T>      the type of the {@link EngineElement}
     * @return         <code>true</code> if the {@link EngineElement} was added, otherwise <code>false</code>
     */
    public <T extends EngineElement> boolean add(T element) {
        element.onAddedToEngine().setEngine(this);
        element.onAddedToEngine().execute();

        if (element instanceof Entity) {
            return addEntity((Entity) element);
        }

        if (element instanceof EntitySystem) {
            return addSystem((EntitySystem) element);
        }

        return false;
    }

    /**
     * Adds an array of {@link EngineElement}s to this {@link Engine}.
     *
     * @param elements  the array of {@link EngineElement}s to add
     * @param <T>       the type of an {@link EngineElement}
     * @return          <code>true</code> if all {@link EngineElement}s were added, otherwise <code>false</code>
     */
    @SafeVarargs
    public final <T extends EngineElement> boolean add(T... elements) {
        boolean allAdded = true;

        for (T element : elements) {
            if (!add(element)) {
                allAdded = false;
            }
        }

        return allAdded;
    }

    /**
     * Removes an {@link EngineElement} from this {@link Engine}.
     *
     * @param element  the {@link EngineElement} to remove
     * @param <T>      the type of the {@link EngineElement}
     * @return         <code>true</code> if the {@link EngineElement} was removed, otherwise <code>false</code>
     */
    public <T extends EngineElement> boolean remove(T element) {
        element.onRemovedFromEngine().setEngine(this);
        element.onRemovedFromEngine().execute();

        if (element instanceof Entity) {
            return removeEntity((Entity) element);
        }

        if (element instanceof EntitySystem) {
            return removeSystem((EntitySystem) element);
        }

        return false;
    }

    /**
     * Removes an array of {@link EngineElement}s from this {@link Engine}.
     *
     * @param elements  the array of {@link EngineElement}s to remove
     * @param <T>       the type of an {@link EngineElement}
     * @return          <code>true</code> if all {@link EngineElement}s were removed, otherwise <code>false</code>
     */
    @SafeVarargs
    public final <T extends EngineElement> boolean remove(T... elements) {
        boolean allRemoved = true;

        for (T element : elements) {
            if (!remove(element)) {
                allRemoved = false;
            }
        }

        return allRemoved;
    }

    public ObservableList getEntities() {
        return entities;
    }

    public ImmutableList<Entity> getEntitiesFor(Class<? extends Component>... components) {
        return entities.filter(components);
    }

    public List<EntitySystem> getSystems() {
        return systems;
    }

    public void update() {
        if (!enabled) {
            return;
        }

        if (updating) {
            throw new IllegalStateException("Cannot update an Engine that is already updating.");
        }

        updating = true;

        try {
            systems.stream().filter(EntitySystem::isEnabled).forEach(system -> system.update());
        } finally {
            updating = false;
        }
    }

    private boolean addEntity(Entity entity) {
        if (entities.contains(entity)) {
            return false;
        }

        if (!entity.onAddedToEngine().isCancelled()) {
            entity.onComponentAdded().add(updateComponentObserver);
            entity.onComponentRemoved().add(updateComponentObserver);
            entities.add(entity);
            return true;
        }

        return false;
    }

    private boolean addSystem(EntitySystem system) {
        if (systems.contains(system)) {
            return false;
        }

        if (!system.onAddedToEngine().isCancelled()) {
            systems.add(system);
            system.setEngine(this);
            return true;
        }

        return false;
    }

    private boolean removeEntity(Entity entity) {
        if (!entities.contains(entity)) {
            return false;
        }

        if (!entity.onRemovedFromEngine().isCancelled()) {
            entity.onComponentAdded().remove(updateComponentObserver);
            entity.onComponentRemoved().remove(updateComponentObserver);
            entities.remove(entity);
            return true;
        }

        return false;
    }

    private boolean removeSystem(EntitySystem system) {
        if (!systems.contains(system)) {
            return false;
        }

        if (!system.onRemovedFromEngine().isCancelled()) {
            systems.remove(system);
            system.setEngine(null);
            return true;
        }

        return false;
    }
}
