package net.exsource.openutils.event;

import net.exsource.openlogger.Logger;

public class TestListener implements Listener {

    private final Logger logger = Logger.getLogger();

    @EventHandler
    public void onTestEvent(TestEvent event) {
        logger.info(event.getExampleName());
    }

}
