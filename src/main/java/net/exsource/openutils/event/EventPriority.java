package net.exsource.openutils.event;

@SuppressWarnings("unused")
public enum EventPriority {

    LOWEST(10),
    LOW(5),
    MEDIUM(4),
    MODERATE(3),
    HIGH(2),
    HIGHEST(1),
    ABSOLUTE_FIRST(0);

    private final Integer rating;

    EventPriority(final Integer rating) {
        this.rating = rating;
    }

    public Integer getRating() {
        return rating;
    }
}
