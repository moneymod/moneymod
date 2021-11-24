package wtf.moneymod.client.api.events;

import wtf.moneymod.eventhandler.event.Event;

public class CameraClipEvent extends Event
{
    public double distance;

    public CameraClipEvent( double distance )
    {
        this.distance = distance;
    }
}
