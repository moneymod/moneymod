package wtf.moneymod.client.api.events;

import wtf.moneymod.eventhandler.event.Event;

public class UpdateWalkingPlayerEvent extends Event {

    private final int stage;

    public UpdateWalkingPlayerEvent(int stage) {
        this.stage = stage;
    }

    public int getStage() { return stage; }

}
