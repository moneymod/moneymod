package wtf.moneymod.client.impl.module.movement;

import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.impl.module.Module;

@Module.Register( label = "Step", cat = Module.Category.MOVEMENT, key = Keyboard.KEY_R)
public class Step extends Module {

    @Override public void onTick() {
        if (!mc.player.isInLava() && !mc.player.isInWater()){
            if (mc.player.onGround)
            mc.player.stepHeight = 2;
        }
    }

    @Override public void onDisable(){
        mc.player.stepHeight = 0.6f;
    }
}
