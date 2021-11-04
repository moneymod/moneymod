package wtf.moneymod.client.impl.module.movement;

import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;

@Module.Register( label = "Step", cat = Module.Category.MOVEMENT )
public class Step extends Module {

    @Value( value = "Height" ) @Bounds( max = 4 ) public double height = 2;

    @Override public void onTick() {
        if (!mc.player.isInLava() && !mc.player.isInWater()) {
            if (mc.player.onGround)
                mc.player.stepHeight = ( float ) height;
        }
    }

    @Override public void onDisable() {
        mc.player.stepHeight = 0.6F;
    }

}
