package wtf.moneymod.client.impl.module.movement;

import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;

@Module.Register( label = "Sprint", cat = Module.Category.MOVEMENT, key = Keyboard.KEY_X )
public class Sprint extends Module {

    @Override public void onTick() {
        if (EntityUtil.INSTANCE.isMoving(mc.player)) mc.player.setSprinting(true);
    }

}
