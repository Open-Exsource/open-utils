package net.exsource.openutils.event;

public interface Cancelable {

    boolean isCancelled();

    void setCancelled(boolean cancelled);

}
