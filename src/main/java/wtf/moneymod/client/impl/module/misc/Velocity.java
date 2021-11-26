package wtf.moneymod.client.impl.module.misc;

import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "Velocity", desc = "Removes player's velocity", cat = Module.Category.MISC )
public class Velocity extends Module
{
    @Handler
    public Listener< PacketEvent.Receive > onReceivePacket = new Listener< >( PacketEvent.Receive.class, event ->
    {
        if ( event.getPacket( ) instanceof SPacketEntityVelocity && ( ( SPacketEntityVelocity ) event.getPacket( ) ).getEntityID( ) == this.mc.player.getEntityId( ) || event.getPacket( ) instanceof SPacketExplosion || event.getPacket( ) instanceof EntityFishHook) {
            event.cancel( );
        }
    } );
}
