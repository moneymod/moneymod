package wtf.moneymod.client.mixin.mixins;

import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.DamageBlockEvent;
import wtf.moneymod.client.mixin.accessors.IPlayerControllerMP;

@Mixin( PlayerControllerMP.class )
public class MixinPlayerControllerMP implements IPlayerControllerMP {

    @Shadow private int blockHitDelay;

    @Inject( method = "onPlayerDamageBlock", at = @At( "HEAD" ), cancellable = true )
    public void onPlayerDamageBlock(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> cir) {
        DamageBlockEvent event = new DamageBlockEvent(posBlock, directionFacing);
        Main.EVENT_BUS.dispatch(event);
        if (event.isCancelled()) {
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Override public void setBlockHitDelay(int delay) {
        this.blockHitDelay = delay;
    }

}
