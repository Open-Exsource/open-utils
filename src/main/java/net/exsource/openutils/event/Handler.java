package net.exsource.openutils.event;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public class Handler {

    private final Object listener;
    private final Method method;
    private final EventHandler eventHandler;

    public Handler(@NotNull Object listener, @NotNull Method method, @NotNull EventHandler eventHandler) {
        this.listener = listener;
        this.method = method;
        this.eventHandler = eventHandler;
    }

    public Object getListener() {
        return listener;
    }

    public Method getMethod() {
        return method;
    }

    public Integer getPriority() {
        return eventHandler.priority().getRating();
    }

    public boolean isIgnoreCancelled() {
        return eventHandler.ignoreCancelled();
    }
}
