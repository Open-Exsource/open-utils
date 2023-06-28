package net.exsource.openutils.enums;

import java.text.SimpleDateFormat;

/**
 * This enum is used for simply set a date format pattern.
 * The pattern ar found at the official {@link SimpleDateFormat} website.
 * You can use this enum for a faster setup pattern.
 * @since 1.0.0
 *
 * @see SimpleDateFormat
 * @author Daniel Ramke
 */
public enum DateFormat {

    DEFAULT("dd.MM.yyyy"),
    DEFAULT_SHORT("dd.MM.yy"),
    EXTENDED_DATE("dd.MMMMM.yyyy"),
    ONLY_TIME("HH:mm:ss"),
    DATE_TIME("dd.MM.yyyy HH:mm:ss"),
    DATE_SHORT_TIME("dd.MM.yy HH:mm"),
    DATE_EXTENDED_TIME("dd.MMMMM.yyyy HH:mm:ss:sss");

    private final String pattern;

    DateFormat(final String pattern) {
        this.pattern = pattern;
    }

    public String getPattern() {
        return pattern;
    }
}
