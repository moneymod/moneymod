package wtf.moneymod.client.api.events;

import net.minecraft.util.MovementInput;
import wtf.moneymod.eventhandler.event.Event;

public class InputUpdateEvent extends Event {

    MovementInput movementInput;

    public InputUpdateEvent(MovementInput movementInput) {
        this.movementInput = movementInput;
    }

    public MovementInput getMovementInput() {
        return movementInput;
    }

}
