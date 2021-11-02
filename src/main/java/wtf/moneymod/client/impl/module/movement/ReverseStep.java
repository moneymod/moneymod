package wtf.moneymod.client.impl.module.movement;

import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.impl.module.Module;

@Module.Register( label = "ReverseStep", cat = Module.Category.MOVEMENT, key = Keyboard.KEY_H)
public class ReverseStep extends Module {

    @Override public void onTick() {
        if (!mc.player.isInWater() || !mc.player.isInLava()) {
            if (mc.player.onGround)
                mc.player.motionY = -0.8;
        }
    }
}
