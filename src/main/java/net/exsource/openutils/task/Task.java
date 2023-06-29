package net.exsource.openutils.task;

import java.util.TimerTask;

public abstract class Task extends TimerTask {

    private boolean alive = true;

    public abstract void runTask();

    @Override
    public void run() {
        runTask();
        alive = false;
    }

    @Override
    public boolean cancel() {
        alive = false;
        return super.cancel();
    }

    public boolean isAlive() {
        return alive;
    }
}
