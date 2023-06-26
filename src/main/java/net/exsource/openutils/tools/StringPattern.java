package net.exsource.openutils.tools;

public enum StringPattern {

    ALPHABETIC_ONLY_LOWERCASE("[^a-z]", 0),
    ALPHABETIC_ONLY_UPPERCASE("[^A-Z]", 1),
    ALPHABETIC("[^A-Za-z]", 2),
    NUMBERS("[^0-9]", 3),
    NUMBERS_AND_ALPHABETIC("[^A-Za-z0-9]", 4),
    UUID_FORMAT("[^A-Za-z0-9]", 5);

    private final String pattern;
    private final Integer id;

    StringPattern(String pattern, Integer id) {
        this.pattern = pattern;
        this.id = id;
    }
    public String getPattern() {
        return pattern;
    }

    public Integer getId() {
        return id;
    }

}
