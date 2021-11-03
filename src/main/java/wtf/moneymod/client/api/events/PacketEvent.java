package wtf.moneymod.client.api.events;

import net.minecraft.network.Packet;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

@Cancelable
public class PacketEvent extends Event {
    private final Packet<?> packet;

    public PacketEvent( final Packet<?> packet ) {
        this.packet = packet;
    }

    public <T extends Packet<?>> T getPacket( ) {
        return ( T ) packet;
    }

    public final static class Receive extends PacketEvent {
        public Receive( final Packet<?> packet ) {
            super( packet );
        }
    }

    public final static class Send extends PacketEvent {
        public Send( final Packet<?> packet ) {
            super( packet );
        }
    }
}