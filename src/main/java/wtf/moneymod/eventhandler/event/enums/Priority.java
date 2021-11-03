package wtf.moneymod.eventhandler.event.enums;

public enum Priority {

    HIGHEST(2),
    HIGH(1),
    MEDIUM(0),
    LOW(-1),
    LOWEST(-2);

    public int priority;

    Priority(int priority) {
        this.priority = priority;
    }

}
