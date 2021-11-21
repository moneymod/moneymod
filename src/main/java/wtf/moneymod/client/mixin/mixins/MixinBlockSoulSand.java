package wtf.moneymod.client.mixin.mixins;

import net.minecraft.block.BlockSoulSand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.SoulSandCollisionEvent;

@Mixin( BlockSoulSand.class )
public class MixinBlockSoulSand
{
    @Inject( method = "onEntityCollision", at = @At( "HEAD" ), cancellable = true )
    public void onEntityCollision( CallbackInfo info )
    {
        SoulSandCollisionEvent event = new SoulSandCollisionEvent( );
        Main.EVENT_BUS.dispatch( event );
        if( event.isCancelled( ) )
            info.cancel( );
    }
}
