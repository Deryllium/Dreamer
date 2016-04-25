package com.dreamburst.dreamer.core;

import com.dreamburst.dreamer.core.events.EngineEvent;

public abstract class EngineElement<T extends EngineElement> {

    /**
     TODO:
     Maybe it would be better to have entity and system events global to the Engine rather than to the individual
     elemenets themselves; there is a higher use-case:

     engine.onEntityAdded().add(new EventExecutor<ElementEvent<Entity>>() { ... });
     engine.onSystemAdded().add(new EventExecutor<ElementEvent<System>>() { ... });


      */


    private EngineEvent<T> addedToEngine;
    private EngineEvent<T> removedFromEngine;

    EngineElement() {
        addedToEngine = new EngineEvent<>(null, (T) this);
        removedFromEngine = new EngineEvent<>(null, (T) this);
    }

    public EngineEvent<T> onAddedToEngine() {
        return addedToEngine;
    }

    public EngineEvent<T> onRemovedFromEngine() {
        return removedFromEngine;
    }
}
