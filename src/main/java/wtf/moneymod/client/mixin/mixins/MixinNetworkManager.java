package wtf.moneymod.client.mixin.mixins;

import io.netty.channel.ChannelHandlerContext;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.moneymod.client.api.events.PacketEvent;
import wtf.moneymod.client.impl.utility.Globals;

@Mixin( NetworkManager.class )
public class MixinNetworkManager implements Globals {

    @Inject( method = "channelRead0", at = @At( "HEAD" ), cancellable = true )
    public void receive( final ChannelHandlerContext context, final Packet<?> packet, final CallbackInfo ci ) {
        if ( mc.player == null && mc.world == null ) return;
        final PacketEvent.Receive event = new PacketEvent.Receive( packet );
        MinecraftForge.EVENT_BUS.post( event );
        if ( event.isCanceled( ) ) {
            ci.cancel( );
        }
    }

    @Inject( method = "sendPacket(Lnet/minecraft/network/Packet;)V", at = @At( "HEAD" ), cancellable = true )
    public void send( final Packet<?> packet, final CallbackInfo ci ) {
        if ( mc.player == null && mc.world == null ) return;
        final PacketEvent.Send event = new PacketEvent.Send( packet );
        MinecraftForge.EVENT_BUS.post( event );
        if ( event.isCanceled( ) ) {
            ci.cancel( );
        }
    }
}