package com.dreamburst.dreamer.delegate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Event {

    private List<EventExecutor> executors;

    public Event() {
        executors = new ArrayList<>();
    }

    public List<EventExecutor> getExecutors() {
        return executors;
    }

    public void add(EventExecutor executor) {
        executors.add(executor);
        sort();
    }

    public boolean remove(EventExecutor executor) {
        if (executors.contains(executor)) {
            executors.remove(executor);

            return true;
        }

        return false;
    }

    public void clear() {
        executors.clear();
    }

    public void execute() {
        sort();
        executors.forEach(executor -> executor.execute(this));
    }

    private void sort() {
        Collections.sort(executors, (one, two) -> getExecutorPriority(one) - getExecutorPriority(two));
    }

    private int getExecutorPriority(EventExecutor executor) {
        try {
            EventHandler handler = executor.getClass()
                    .getDeclaredMethod("execute", Event.class)
                    .getAnnotation(EventHandler.class);

            if (handler != null) {
                return handler.priority();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return 0;
    }
}
