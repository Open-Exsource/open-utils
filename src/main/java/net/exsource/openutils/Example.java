package net.exsource.openutils;

import net.exsource.openlogger.Logger;
import net.exsource.openutils.io.IOController;
import net.exsource.openutils.io.controller.PropertiesController;

public class Example {

    private static final Logger logger = Logger.getLogger();

    public static void main(String[] args) {
        PropertiesController controller = (PropertiesController) IOController.fromArgs(args, PropertiesController.class);
        if(controller == null) {
            logger.warn("NOT GOOD...");
            return;
        }
        logger.info(controller.getClass().getSimpleName());
    }

}
