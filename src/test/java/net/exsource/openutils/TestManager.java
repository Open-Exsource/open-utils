package net.exsource.openutils;

import net.exsource.openlogger.Logger;
import net.exsource.openutils.event.EventManager;
import net.exsource.openutils.event.TestEvent;
import net.exsource.openutils.event.TestListener;
import net.exsource.openutils.io.IOController;
import net.exsource.openutils.io.controller.IniController;
import net.exsource.openutils.io.controller.PropertiesController;
import net.exsource.openutils.task.PeriodTask;
import net.exsource.openutils.task.Task;
import net.exsource.openutils.task.TaskManager;
import net.exsource.openutils.task.TaskTimer;
import net.exsource.openutils.tools.Commons;
import net.exsource.openutils.enums.DateFormat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TestManager {

    private static final Logger logger = Logger.getLogger();

    private final String[] programArgs = new String[]{"ui-style=windows", "ui-logic=threaded"};

    @BeforeAll
    static void testSetup() {
        Logger.enableDebug(true);
    }

    @Test
    void checkEventCanNeCalled() {
        EventManager.registerListener(new TestListener());

        TestEvent event = new TestEvent();
        EventManager.callEvent(event);
    }

    @Test
    void checkDateFormats() {
        Date date = new Date();
        for (DateFormat format : DateFormat.values()) {
            logger.debug(format.name() + " -> " + Commons.parseDateToString(date, format));
        }
    }

    @Test
    void checkProgramArgumentsAsProperties() throws IOException {
        PropertiesController controller = IOController.fromArgs(programArgs, PropertiesController.class);
        if(controller == null) {
            logger.warn("Test:checkProgramArguments -> can't create controller!");
            return;
        }

        logger.debug("====================> Properties <====================");
        for(String entries : controller.getProperties()) {
            logger.debug(entries);
        }
        logger.debug("====================> Properties <====================");
    }

    @Test
    void checkProgramArgumentsAsIni() throws IOException {
        IniController controller = IOController.fromArgs(programArgs, IniController.class);
        if(controller == null) {
            logger.warn("Test:checkProgramArguments -> can't create controller!");
            return;
        }

        logger.debug("====================> Ini <====================");
        logger.debug("ui-style -> " + controller.getValue("ui-style", String.class));
        logger.debug("ui-logic -> " + controller.getValue("ui-logic", String.class));
        logger.debug("====================> Ini <====================");
    }

    @Test
    void checkTaskRunLater() throws InterruptedException {
        TaskTimer timer = TaskManager.runTaskLater(new Task() {
            @Override
            public void runTask() {
                logger.debug("This text is displayed after 5 seconds!");
            }
        }, 4, TimeUnit.SECONDS);

        Task task = timer.getTask();
        while (task.isAlive()) {
            TimeUnit.SECONDS.sleep(1);
        }
        logger.debug("Time is up: " + task.isAlive());
        TaskManager.flush(timer);
    }

    @Test
    void checkRunTask() throws InterruptedException {
        TaskTimer timer = TaskManager.runTask(new PeriodTask() {
            int index = 0;

            @Override
            public void runTask() {
                logger.debug("I called every second...");
                index++;
                if(index >= 5) {
                    cancel();
                }
            }
        }, 0, 1, TimeUnit.SECONDS);

        Task task = timer.getTask();
        while (task.isAlive()) {
            TimeUnit.SECONDS.sleep(1);
        }
        logger.debug("Time is up! Dump ready: " + TaskManager.getNeedDumped().size());
        TaskManager.cleanup();
    }

}
