package wtf.moneymod.client.api.events;

import wtf.moneymod.eventhandler.event.Event;

public class MoveEvent extends Event {

    public double motionX, motionY, motionZ;

    public MoveEvent( final double motionX, final double motionY, final double motionZ ) {
        this.motionX = motionX;
        this.motionY = motionY;
        this.motionZ = motionZ;
    }
}