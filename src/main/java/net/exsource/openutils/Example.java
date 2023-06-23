package net.exsource.openutils;

import net.exsource.openlogger.Logger;
import net.exsource.openutils.io.controller.IniController;

import java.io.File;
import java.io.IOException;

public class Example {

    private static final Logger logger = Logger.getLogger();

    public static void main(String[] args) throws IOException {
        Logger.enableDebug(true);
        IniController controller = new IniController();
        controller.load(new File("example.ini"));

        logger.info(controller.getClass().getSimpleName() + " -> " + controller.getValue( "ui-style", String.class));
        logger.info(controller.getClass().getSimpleName() + " -> " + controller.getValue( "ui-scale", int.class));
        logger.info(controller.getClass().getSimpleName() + " -> " + controller.getValue( "ui-antialiasing", Boolean.class));
    }

}
