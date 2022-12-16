package wtf.moneymod.client.impl.module.movement;

import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.api.events.MotionUpdateEvent;
import wtf.moneymod.client.api.events.MoveEvent;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "Sprint", cat = Module.Category.MOVEMENT)
public class Sprint extends Module {

    @Override public void onTick() {
        if (EntityUtil.INSTANCE.isMoving(mc.player) && !mc.player.isSneaking() && !mc.player.collidedHorizontally) mc.player.setSprinting(true);
    }

}
