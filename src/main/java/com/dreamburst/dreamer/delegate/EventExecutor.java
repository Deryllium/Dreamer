package com.dreamburst.dreamer.delegate;

public interface EventExecutor<T extends Event> {
    void execute(T event);
}
