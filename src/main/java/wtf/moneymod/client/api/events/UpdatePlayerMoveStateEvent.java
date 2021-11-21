package wtf.moneymod.client.api.events;

import net.minecraft.util.MovementInput;
import wtf.moneymod.eventhandler.event.Event;

public class UpdatePlayerMoveStateEvent extends Event
{
    private MovementInput input;

    public UpdatePlayerMoveStateEvent( MovementInput input )
    {
        this.input = input;
    }

    public MovementInput getMovementInput( )
    {
        return input;
    }
}