package net.exsource.openutils.task;

public abstract class PeriodTask extends Task {

    private boolean alive = true;

    public abstract void runTask();

    @Override
    public void run() {
        runTask();
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
