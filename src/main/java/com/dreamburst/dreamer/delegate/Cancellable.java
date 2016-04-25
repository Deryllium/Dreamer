package com.dreamburst.dreamer.delegate;

public interface Cancellable {
    boolean isCancelled();
    void setCancelled(boolean cancelled);
}
