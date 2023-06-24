package net.exsource.openutils.event;

import net.exsource.openlogger.Logger;
import net.exsource.openlogger.util.ConsoleColor;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

@SuppressWarnings("unused")
public class EventManager {

    private static final Logger logger = Logger.getLogger();

    private static final Collection<Listener> listeners;

    static {
        listeners = new LinkedHashSet<>();
    }

    public static void registerListeners(Listener... listeners) {
        if(listeners == null)
            return;
        registerListeners(Arrays.asList(listeners));
    }

    public static void registerListeners(List<Listener> listeners) {
        if(listeners == null)
            return;
        if(listeners.isEmpty())
            return;
        for(Listener listener : listeners) {
            registerListener(listener);
        }
    }

    public static void registerListener(Listener listener) {
        if(listeners.contains(listener))
            return;
        listeners.add(listener);
        logger.debug("Added new listener: " + ConsoleColor.CYAN + listener.getClass().getSimpleName() + ConsoleColor.RESET);
    }

    public static void unregisterAllListeners() {
        if(listeners.isEmpty())
            return;
        listeners.clear();
    }

    public static void unregisterListener(Listener listener) {
        if(!listeners.contains(listener))
            return;
        listeners.remove(listener);
        logger.debug("Remove listener: " + ConsoleColor.CYAN + listener.getClass().getSimpleName() + ConsoleColor.RESET);
    }

    public static void callEvent(Event event) {
        if(event == null)
            return;
        logger.debug("Calling event -> " + ConsoleColor.CYAN + event.getEventName() + ConsoleColor.RESET);
        for(Listener listener : listeners) {
            Class<? extends Listener> caller = listener.getClass();
            final Method[] methods = caller.getDeclaredMethods();

            try {
                for (Method method : methods) {
                    EventHandler handler = method.getAnnotation(EventHandler.class);
                    if (handler == null)
                        continue;
                    if (method.getParameterTypes().length != 1
                            || !method.getParameterTypes()[0].isAssignableFrom(event.getClass()))
                        continue;
                    method.invoke(listener, event);
                }
            } catch (Exception exception) {
                logger.fatal(exception);
            }
        }
    }

}
