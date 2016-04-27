package com.dreamburst.dreamer.systems;

import com.dreamburst.dreamer.core.*;
import com.dreamburst.dreamer.core.events.EngineEvent;
import com.dreamburst.dreamer.delegate.EventExecutor;
import com.dreamburst.dreamer.delegate.EventHandler;
import com.dreamburst.dreamer.core.ImmutableList;

public abstract class IteratingSystem extends EntitySystem {

    private ImmutableList<Entity> entities;
    private Class<? extends Component>[] components;

    private final EventExecutor<EngineEvent<IteratingSystem>> addEventExecutor;
    private final EventExecutor<EngineEvent<IteratingSystem>> removeEventExecutor;

    public IteratingSystem() {
        addEventExecutor = new EventExecutor<EngineEvent<IteratingSystem>>() {
            @EventHandler(priority = -1)
            public void execute(EngineEvent<IteratingSystem> event) {
                entities = event.getEngine().getEntitiesFor(components);
            }
        };

        removeEventExecutor = new EventExecutor<EngineEvent<IteratingSystem>>() {
            @EventHandler(priority = -1)
            public void execute(EngineEvent<IteratingSystem> event) {
                entities = null;
            }
        };

        try {
            Components annotation = getClass()
                    .getAnnotation(Components.class);

            components = annotation.value();

            if (components.length > 0) {
                onAddedToEngine().add(addEventExecutor);
                onRemovedFromEngine().add(removeEventExecutor);
            }

        } catch (Exception ignored) {
        }
    }

    public ImmutableList<Entity> getEntities() {
        return entities;
    }

    @Override
    public void update(float delta) {
        for (Entity entity : entities) {
            update(entity, delta);
        }
    }

    public void update(Entity entity, float delta) {}
}
