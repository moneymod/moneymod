package wtf.moneymod.client.impl.module.movement;

import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;

@Module.Register( label = "FastSwim", cat = Module.Category.MOVEMENT)
public class FastSwim extends Module {

    @Override public void onTick() {

        if (mc.player.isInLava() || mc.player.isInWater() && EntityUtil.INSTANCE.isMoving(mc.player)){
            mc.player.setSprinting(true);
            if (mc.gameSettings.keyBindJump.isKeyDown()) mc.player.motionY = 0.098;
        } else return;
    }

}
