package wtf.moneymod.client.api.events;

import wtf.moneymod.eventhandler.event.Event;

public class MotionUpdateEvent extends Event
{
    public MotionUpdateEvent( float rotationYaw, float rotationPitch, double posX, double posY, double posZ, boolean onGround, boolean noClip, int stage )
    {
        this.rotationYaw = rotationYaw;
        this.rotationPitch = rotationPitch;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        this.onGround = onGround;
        this.noClip = noClip;
        this.stage = stage;
    }

    public float rotationYaw;
    public float rotationPitch;
    public double posX;
    public double posY;
    public double posZ;
    public boolean onGround;
    public boolean noClip;
    public int stage;
}
