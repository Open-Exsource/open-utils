package net.exsource.openutils.event;

import net.exsource.openlogger.Logger;

public class TestListener {

    private static final Logger logger = Logger.getLogger();

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onExampleEventHighest(TestEvent event) {
        logger.debug("Event -> " + event.getName() + " [ 1 ]");
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onExampleEventLowest(TestEvent event) {
        logger.debug("Event -> " + event.getName() + " [ 4 ]");
    }

    @EventHandler(priority = EventPriority.MEDIUM)
    public void onExampleEventMedium(TestEvent event) {
        logger.debug("Event -> " + event.getName() + " [ 3 ]");
    }

    @EventHandler
    public void onExampleEventMonitor(TestEvent event) {
        logger.debug("Event -> " + event.getName() + " [ 2 ]");
    }

}
