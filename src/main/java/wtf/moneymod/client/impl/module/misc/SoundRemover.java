package wtf.moneymod.client.impl.module.misc;

import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "SoundRemover", desc = "Mutes various sounds", cat = Module.Category.MISC )
public class SoundRemover extends Module
{
    @Value( "ChorusTeleport" ) public boolean chorusteleport = false;
    @Value( "TotemPop" ) public boolean totempop = false;
    @Value( "Explosions" ) public boolean explosions = false;

    @Handler
    public Listener< PacketEvent.Receive > onReceivePacket = new Listener< >( PacketEvent.Receive.class, event ->
    {
        final SPacketSoundEffect packet = event.getPacket( );
        if ( chorusteleport && packet.getSound( ) == SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT ) {
            event.cancel( );
        }
        if ( explosions && packet.getSound( ) == SoundEvents.ENTITY_GENERIC_EXPLODE ) {
            event.cancel( );
        }
    } );
}
