package net.exsource.openutils.task;

import net.exsource.openlogger.Logger;
import net.exsource.openutils.enums.StringPattern;
import net.exsource.openutils.tools.Commons;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class TaskManager {

    private static final Logger logger = Logger.getLogger();
    private static final Map<String, TaskTimer> tasks = new HashMap<>();

    public static TaskTimer runTaskLater(@NotNull TimerTask task, long time, TimeUnit unit) {
        long delay = Commons.convertTimeToMillis(time, unit);
        if(delay <= 0) {
            delay = 1000L;
        }

        TaskTimer timer = new TaskTimer();
        timer.schedule(task, delay);
        tasks.put(timer.getName(), timer);
        logger.debug("Added new task: " + timer.getName());
        return timer;
    }

    public static TaskTimer runTask(@NotNull TimerTask task, long delay, long period, TimeUnit unit) {
        long callLater = Commons.convertTimeToMillis(delay, unit);
        long runTime = Commons.convertTimeToMillis(period, unit);
        if(callLater < 0) {
            callLater = 0L;
        }
        if(runTime <= 0) {
            runTime = 1000L;
        }

        TaskTimer timer = new TaskTimer();
        timer.scheduleAtFixedRate(task, callLater, runTime);
        tasks.put(timer.getName(), timer);
        logger.debug("Added new task: " + timer.getName());
        return timer;
    }

    public static void flush(TaskTimer timer) {
        if(timer == null) {
            logger.warn("Can't flush a timer that's ar null!");
            return;
        }

        if(!timer.isAlive() && existTask(timer.getName())) {
            tasks.remove(timer.getName());
            logger.debug("Remove TaskTimer: " + timer.getName());
        }
    }

    public static void flushAllDumped() {
        for(TaskTimer timer : getNeedDumped()) {
            flush(timer);
        }
    }

    public static void cleanup() {
        flushAllDumped();
        List<TaskTimer> cleanerList = new ArrayList<>();
        for(Map.Entry<String, TaskTimer> timers : tasks.entrySet()) {
            TaskTimer current = timers.getValue();
            if(current.isAlive()) {
                current.getTask().cancel();
            }
            cleanerList.add(current);
        }

        for(TaskTimer timer : cleanerList) {
            flush(timer);
        }
        cleanerList.clear();
        logger.debug("Clearing all task successfully!");
    }

    public static TaskTimer getByName(@NotNull String name) {
        if(tasks.containsKey(name)) {
            return tasks.get(name);
        }
        return null;
    }

    public static boolean existTask(@NotNull String name) {
        return getByName(name) != null;
    }

    public static List<TaskTimer> getNeedDumped() {
        List<TaskTimer> dump = new ArrayList<>();
        for(Map.Entry<String, TaskTimer> timer : tasks.entrySet()) {
            if(!timer.getValue().isAlive()) {
                dump.add(timer.getValue());
            }
        }
        return dump;
    }

    public static String generateSerialID() {
        String serialID = "TaskTimer-" + Commons.generateString(StringPattern.NUMBERS, 4);
        if(existTask(serialID) && !tasks.isEmpty()) {
            return generateSerialID();
        }
        return serialID;
    }

}
