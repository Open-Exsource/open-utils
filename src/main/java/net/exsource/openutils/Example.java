package net.exsource.openutils;

import net.exsource.openlogger.Logger;
import net.exsource.openutils.io.controller.PropertiesController;
import net.exsource.openutils.enums.OperationSystem;

import java.io.File;
import java.io.IOException;

public class Example {

    private static final Logger logger = Logger.getLogger();

    public static void main(String[] args) throws IOException {
        Logger.enableDebug(true);
        PropertiesController controller = new PropertiesController();
        controller.load(new File("example.properties"));
        logger.debug("Operating System -> " + OperationSystem.getCurrent());

        controller.add("writer-test", "testedValue");
        controller.remove("test-string");
        controller.saveAs("properties/test.properties");
    }

}
