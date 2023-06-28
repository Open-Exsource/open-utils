package net.exsource.openutils.io;

import net.exsource.openlogger.Logger;
import net.exsource.openutils.io.controller.IniController;
import net.exsource.openutils.io.controller.PropertiesController;
import net.exsource.openutils.tools.Commons;
import net.exsource.openutils.enums.StringPattern;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

@SuppressWarnings("unused")
public abstract class IOController {

    protected static final Logger logger = Logger.getLogger();

    private final String ID;
    private File resource;

    public IOController() {
        this.ID = getClass().getSimpleName() + "-" + Commons.generateString(StringPattern.NUMBERS, 3);
        this.resource = null;
    }

    public abstract void load(File file) throws IOException;

    public abstract void load(String[] args) throws IOException;

    public abstract boolean hasKey(@NotNull String key);

    public abstract <T> T getValue(@NotNull String key, Class<T> cast);

    protected void setResource(@NotNull File file) {
        this.resource = file;
    }

    public File getResource() {
        return resource;
    }

    public String getID() {
        return ID;
    }

    @SuppressWarnings("unchecked")
    public static <T> T fromArgs(String[] args, Class<T> type) throws IOException {
        switch (type.getSimpleName()) {
            case "PropertiesController" -> {
                PropertiesController controller = new PropertiesController();
                controller.load(args);
                return (T) controller;
            }
            case "IniController" -> {
                IniController controller = new IniController();
                controller.load(args);
                return (T) controller;
            }
            default -> {
                return null;
            }
        }
    }

    protected String covertToReadableValue(final String string) {
        return string.replaceAll("^\"(.*)\"$", "$1")
                .replaceAll("^'(.*)'$", "$1")
                .replaceAll("\\\\\"", "\"")
                .replaceAll("\\\\'", "'")
                .replaceAll("\\\\\\\\", "\\\\")
                .replaceAll("\\\\t", "\t")
                .replaceAll("\\\\r", "\r")
                .replaceAll("\\\\n", "\n")
                .replaceAll("\\\\0", "\0")
                .replaceAll("\\\\b", "\b")
                .replaceAll("\\\\f", "\f")
                .replaceAll("\\\\#", "#")
                .replaceAll("\\\\=", "=")
                .replaceAll("\\\\:", ":");
    }
}
