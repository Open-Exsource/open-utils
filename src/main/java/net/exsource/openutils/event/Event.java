package net.exsource.openutils.event;

import org.jetbrains.annotations.NotNull;

public abstract class Event {

    private String name;

    @NotNull
    public String getEventName() {
        if(name == null)
            name = getClass().getSimpleName();
        return name;
    }

}
