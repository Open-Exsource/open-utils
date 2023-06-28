package net.exsource.openutils.event;

import net.exsource.openlogger.Logger;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@SuppressWarnings("unused")
public class EventManager {

    private static final Logger logger = Logger.getLogger();

    private static final Map<Class<? extends Event>, Set<Handler>> handlers;

    static {
        handlers = new HashMap<>();
    }

    public static void registerListener(@NotNull Object listener) {
        for(Method method : listener.getClass().getDeclaredMethods()) {
            if(isEventHandler(method)) {
                registerListener(method, listener);
            }
        }
    }

    public static void unregisterListener(@NotNull Object listener) {
        for(Iterator<Set<Handler>> iterator = handlers.values().iterator(); iterator.hasNext(); ) {
            Set<Handler> entries = iterator.next();
            entries.removeIf(handler -> handler.getListener().equals(listener));

            if(entries.isEmpty()) {
                iterator.remove();
            }
        }
    }

    public static void unregisterAllListeners() {
        handlers.clear();
    }

    public static void callEvent(Event event) {
        Event out = callReturnedEvent(event);
        logger.debug(out.getName() + ", called!");
    }

    public static <T extends Event> T callReturnedEvent(T event) {
        List<Handler> sortedHandlers = new ArrayList<>();
        Class<?> cls = event.getClass();

        while (cls != Object.class) {
            Set<Handler> classHandlers = handlers.get(cls);

            if(classHandlers != null) {
                sortedHandlers.addAll(classHandlers);
            }

            for(Class<?> i : cls.getInterfaces()) {
                Set<Handler> interfaceHandlers = handlers.get(i);
                if(interfaceHandlers != null) {
                    sortedHandlers.addAll(interfaceHandlers);
                }
            }

            cls = cls.getSuperclass();
        }

        if(!sortedHandlers.isEmpty()) {
            sortedHandlers.sort(Comparator.comparing(Handler::getPriority));

            Cancelable cancelable = event instanceof Cancelable ? (Cancelable) event : null;
            for(Handler handler : sortedHandlers) {
                if(cancelable != null && !handler.isIgnoreCancelled() && cancelable.isCancelled()) {
                    continue;
                }

                invoke(handler, event);
            }
        }

        return event;
    }

    private static void registerListener(Method method, Object listener) {
        Class<? extends Event> cls = method.getParameterTypes()[0].asSubclass(Event.class);
        if(!method.isAccessible()) {
            method.setAccessible(true);
        }

        Handler handler = new Handler(listener, method, method.getAnnotation(EventHandler.class));
        handlers.computeIfAbsent(cls, k -> new HashSet<>()).add(handler);
    }

    private static boolean isEventHandler(Method method) {
        Class<?>[] parameters = method.getParameterTypes();
        return parameters.length == 1 && Event.class.isAssignableFrom(parameters[0]) && method.isAnnotationPresent(EventHandler.class);
    }

    private static void invoke(Handler handler, Event event) {
        try {
            handler.getMethod().invoke(handler.getListener(), event);
        } catch (IllegalAccessException | InvocationTargetException exception) {
            logger.error(exception);
        }
    }

}
