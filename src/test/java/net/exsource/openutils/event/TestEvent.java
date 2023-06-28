package net.exsource.openutils.event;


public class TestEvent implements Event, Cancelable {

    private boolean cancelled;

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public String getName() {
        return getClass().getSimpleName();
    }
}
