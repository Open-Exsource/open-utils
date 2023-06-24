package net.exsource.openutils.event;


public class TestEvent extends Event {

    private final String exampleName;

    public TestEvent(String exampleName) {
        this.exampleName = exampleName;
    }

    public String getExampleName() {
        return exampleName;
    }

}
