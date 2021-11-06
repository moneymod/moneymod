package wtf.moneymod.client.mixin.mixins;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.tutorial.Tutorial;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.Sys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.DamageBlockEvent;
import wtf.moneymod.client.impl.module.global.Global;
import wtf.moneymod.client.impl.module.player.SpeedMine;
import wtf.moneymod.client.impl.utility.Globals;
import wtf.moneymod.client.mixin.accessors.IPlayerControllerMP;

@Mixin( PlayerControllerMP.class )
public class MixinPlayerControllerMP implements IPlayerControllerMP, Globals {

    @Shadow private int blockHitDelay;

    @Inject(method = "clickBlock", at = @At("HEAD"), cancellable = true)
    private void clickBlock(BlockPos posBlock, EnumFacing directionFacing, CallbackInfoReturnable<Boolean> cir) {
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
