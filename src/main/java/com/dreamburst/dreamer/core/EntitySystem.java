package com.dreamburst.dreamer.core;

public abstract class EntitySystem extends EngineElement<EntitySystem> {

    // TODO: Document?

    private boolean enabled;
    private Engine engine;

    public EntitySystem() {
        this.enabled = true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Engine getEngine() {
        return engine;
    }

    void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void enable() {
        enabled = true;
    }

    public void disable() {
        enabled = false;
    }

    public void update(float delta) {
    }

}
