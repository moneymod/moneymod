package wtf.moneymod.client.api.events;

import wtf.moneymod.eventhandler.event.Event;

public class Render3DEvent extends Event
{
    private float partialTicks;

    public Render3DEvent( float partialTicks )
    {
        this.partialTicks = partialTicks;
    }

    public float getPartialTicks( )
    {
        return partialTicks;
    }
}
