package net.exsource.openutils.event;

@SuppressWarnings("unused")
public enum EventPriority {

    LOWEST(0),
    LOW(1),
    MEDIUM(2),
    MODERATE(3),
    HIGH(4),
    HIGHEST(5),
    ABSOLUTE_FIRST(10);

    private final Integer rating;

    EventPriority(final Integer rating) {
        this.rating = rating;
    }

    public Integer getRating() {
        return rating;
    }
}
