package net.exsource.openutils.task;

import net.exsource.openutils.tools.Commons;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TaskTimer extends Timer {

    private final String name;
    private Task task;

    private long delay;
    private long period;

    public TaskTimer() {
        this(TaskManager.generateSerialID());
    }

    public TaskTimer(String name) {
        super(name);
        this.name = name;
        this.task = null;
        this.delay = 0L;
        this.period = 0L;
    }

    @Override
    public void schedule(TimerTask task, long delay) {
        super.schedule(task, delay);
        this.task = (Task) task;
        this.delay = delay;
    }

    @Override
    public void schedule(TimerTask task, long delay, long period) {
        super.schedule(task, delay, period);
        this.task = (Task) task;
        this.delay = delay;
        this.period = period;
    }

    @Override
    public void scheduleAtFixedRate(TimerTask task, long delay, long period) {
        super.scheduleAtFixedRate(task, delay, period);
        this.task = (Task) task;
        this.delay = delay;
        this.period = period;
    }

    public Task getTask() {
        return task;
    }

    public boolean isAlive() {
        return task != null && task.isAlive();
    }

    public String getName() {
        return name;
    }

    public long getDelay(TimeUnit unit) {
        return Commons.convertMillisToUnit(delay, unit);
    }

    public long getDelay() {
        return getDelay(TimeUnit.MILLISECONDS);
    }

    public long getPeriod(TimeUnit unit) {
        return Commons.convertMillisToUnit(period, unit);
    }

    public long getPeriod() {
        return getPeriod(TimeUnit.MILLISECONDS);
    }
}
