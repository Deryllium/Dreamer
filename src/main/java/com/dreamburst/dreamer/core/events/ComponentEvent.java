package com.dreamburst.dreamer.core.events;

import com.dreamburst.dreamer.core.Component;
import com.dreamburst.dreamer.core.Entity;
import com.dreamburst.dreamer.delegate.Event;

public class ComponentEvent extends Event {

    private Entity entity;
    private Component component;

    public ComponentEvent(Entity entity) {
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }
}
