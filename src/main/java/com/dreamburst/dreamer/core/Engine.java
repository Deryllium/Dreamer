package com.dreamburst.dreamer.core;

import com.dreamburst.dreamer.core.events.ComponentEvent;
import com.dreamburst.dreamer.delegate.EventExecutor;
import com.dreamburst.dreamer.util.ImmutableList;

import java.util.ArrayList;
import java.util.List;

public class Engine {

    private boolean enabled;
    private boolean updating;

    private List<Entity> entities;
    private List<EntitySystem> systems;

    private ComponentMapper componentMapper;

    public Engine() {
        entities = new ArrayList<>();
        systems = new ArrayList<>();

        componentMapper = new ComponentMapper();

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

    public List<Entity> getEntities() {
        return entities;
    }

    public ImmutableList<Entity> getEntitiesFor(Class<? extends Component>... components) {
        return componentMapper.getEntitiesFor(entities, components);
    }

    public List<EntitySystem> getSystems() {
        return systems;
    }

    public void update(float delta) {
        if (!enabled) {
            return;
        }

        if (updating) {
            throw new IllegalStateException("Cannot update an Engine that is already updating.");
        }

        updating = true;

        try {
            systems.stream().filter(EntitySystem::isEnabled).forEach(system -> system.update(delta));
        } finally {
            updating = false;
        }
    }

    private boolean addEntity(Entity entity) {
        if (entities.contains(entity)) {
            return false;
        }

        if (!entity.onAddedToEngine().isCancelled()) {
            entity.onComponentAdded().add(new EventExecutor<ComponentEvent>() {
                @Override
                public void execute(ComponentEvent event) {
                    updateComponentMapper();
                }
            });

            entities.add(entity);
            updateComponentMapper();

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

    private void updateComponentMapper() {
        componentMapper.update(entities);
    }
}
