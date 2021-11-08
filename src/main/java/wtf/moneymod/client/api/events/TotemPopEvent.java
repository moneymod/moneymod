package wtf.moneymod.client.api.events;

import net.minecraft.entity.player.EntityPlayer;
import wtf.moneymod.eventhandler.event.Event;

public class TotemPopEvent extends Event {

    private final EntityPlayer entityPlayerSP;

    public TotemPopEvent( EntityPlayer entityPlayerSP ) {
        this.entityPlayerSP = entityPlayerSP;
    }

    public EntityPlayer getEntityPlayerSP( ) {return entityPlayerSP;}
}