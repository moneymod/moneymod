package wtf.moneymod.eventhandler.event;

import wtf.moneymod.eventhandler.event.enums.Era;

public abstract class Event {

    private boolean cancelled;

    private Era era = Era.NONE;

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void setCancelled(final boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void cancel() {
        setCancelled(true);
    }

    public void setEra(Era era) {
        this.era = era;
    }

    public Era getEra() {
        return era;
    }

}
