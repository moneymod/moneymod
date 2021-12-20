package wtf.moneymod.client.impl.module.movement;

import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.mixin.mixins.ducks.*;

@Module.Register( label = "NoJumpDelay", cat = Module.Category.MOVEMENT)
public class NoJumpDelay extends Module {

    @Override
    public void onTick(){
        if (nullCheck()) return;

        ((AccessorEntityLivingBase) mc.player).setJumpTicks(0);
        ((AccessorEntity) mc.player).setNextStepDistance(0);
    }
}
