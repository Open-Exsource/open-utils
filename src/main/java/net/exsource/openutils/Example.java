package net.exsource.openutils;

import net.exsource.openlogger.Logger;
import net.exsource.openutils.io.controller.PropertiesController;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Example {

    private static final Logger logger = Logger.getLogger();

    public static void main(String[] args) throws IOException {
        Logger.enableDebug(true);
        PropertiesController controller = new PropertiesController();
        controller.load(new File("example.properties"));

        logger.debug(controller.getClass().getSimpleName() + " -> " + controller.getValue("test-string", String.class));
        logger.debug(controller.getClass().getSimpleName() + " -> " + controller.getValue("test-number", Integer.class));
        logger.debug(controller.getClass().getSimpleName() + " -> " + Arrays.toString(controller.getValueAsArray("test-array")));
        logger.debug(controller.getClass().getSimpleName() + " -> " + controller.getValue("test-map", String.class));
    }

}
