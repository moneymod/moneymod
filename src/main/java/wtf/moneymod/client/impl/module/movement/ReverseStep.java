package wtf.moneymod.client.impl.module.movement;

import wtf.moneymod.client.impl.module.Module;

@Module.Register( label = "ReverseStep", cat = Module.Category.MOVEMENT )
public class ReverseStep extends Module {

    @Override public void onTick() {
        if (!mc.player.isInWater() || !mc.player.isInLava()) {
            if (mc.player.onGround)
            mc.player.motionY = -9;
        }
    }
}
