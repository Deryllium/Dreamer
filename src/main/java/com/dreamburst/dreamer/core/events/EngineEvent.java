package com.dreamburst.dreamer.core.events;

import com.dreamburst.dreamer.core.Engine;
import com.dreamburst.dreamer.core.EngineElement;
import com.dreamburst.dreamer.delegate.Cancellable;
import com.dreamburst.dreamer.delegate.Event;

public class EngineEvent<T extends EngineElement> extends Event implements Cancellable {

    private Engine engine;
    private T element;

    private boolean cancelled;

    public EngineEvent(Engine engine, T element) {
        this.engine = engine;
        this.element = element;
    }

    public Engine getEngine() {
        return engine;
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public T getElement() {
        return element;
    }

    public void setElement(T element) {
        this.element = element;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }
}
