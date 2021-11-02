package wtf.moneymod.client.other.module.movement;

import club.cafedevelopment.reflectionsettings.annotation.Setting;
import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.other.module.Module;
import wtf.moneymod.client.utility.impl.world.EntityUtil;

@Module.Register( label = "Sprint", cat = Module.Category.MOVEMENT, key = Keyboard.KEY_X )
public class Sprint extends Module {

    @Override public void onTick() {
        if (EntityUtil.INSTANCE.isMoving(mc.player)) mc.player.setSprinting(true);
    }

}
