package wtf.moneymod.client.mixin.mixins;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.BreakBlockEvent;

@Mixin( Block.class )
public class MixinBlock {

    @Inject( method = "removedByPlayer", at = @At( "TAIL" ), remap = false )
    private void removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest, CallbackInfoReturnable<Boolean> cir) {
        BreakBlockEvent breakBlockEvent = new BreakBlockEvent(world, player, pos, state);
        Main.EVENT_BUS.dispatch(breakBlockEvent);
    }

}
