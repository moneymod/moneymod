package wtf.moneymod.client.impl.module.movement;

import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.api.events.MoveEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "FastSwim", cat = Module.Category.MOVEMENT)
public class FastSwim extends Module {
    @Value(value = "Speed") @Bounds(min = 0, max = 0.50f) public float speed = 0.25f;
    @Handler
    public Listener<MoveEvent> moveListener = new Listener<>(MoveEvent.class, event -> {
        if (mc.player.isInLava() || mc.player.isInWater() && EntityUtil.INSTANCE.isMoving(mc.player)) {
            mc.player.setSprinting(true);
            if (mc.gameSettings.keyBindJump.isKeyDown()) mc.player.motionY = 0.098;
            EntityUtil.INSTANCE.setVanilaSpeed(event, speed);
        }
    });
}