package wtf.moneymod.client.api.events;

import wtf.moneymod.eventhandler.event.Event;

public class BlockReachEvent extends Event
{
    private float distance;

    public BlockReachEvent( float distance )
    {
        this.distance = distance;
    }

    public float getDistance( )
    {
        return distance;
    }

    public void setDistance( float distance )
    {
        this.distance = distance;
    }
}
