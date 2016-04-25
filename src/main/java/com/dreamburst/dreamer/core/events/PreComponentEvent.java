package com.dreamburst.dreamer.core.events;

import com.dreamburst.dreamer.core.Entity;
import com.dreamburst.dreamer.delegate.Cancellable;

public class PreComponentEvent extends ComponentEvent implements Cancellable {

    private boolean cancelled;

    public PreComponentEvent(Entity entity) {
        super(entity);
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
