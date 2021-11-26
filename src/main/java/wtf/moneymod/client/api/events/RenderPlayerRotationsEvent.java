package wtf.moneymod.client.api.events;

import wtf.moneymod.eventhandler.event.Event;

public class RenderPlayerRotationsEvent extends Event
{
    private float yaw;
    private float pitch;

    public RenderPlayerRotationsEvent( float yaw, float pitch )
    {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public float getYaw( )
    {
        return yaw;
    }

    public float getPitch( )
    {
        return pitch;
    }

    public void setYaw( float yaw )
    {
        this.yaw = yaw;
    }

    public void setPitch( float pitch )
    {
        this.pitch = pitch;
    }
}
