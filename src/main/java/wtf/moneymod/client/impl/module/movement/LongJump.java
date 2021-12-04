package wtf.moneymod.client.impl.module.movement;

import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.events.MoveEvent;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;
import wtf.moneymod.eventhandler.listener.Handler;
import wtf.moneymod.eventhandler.listener.Listener;

@Module.Register( label = "LongJump", cat = Module.Category.MOVEMENT)
public class LongJump extends Module {

    @Value(value = "Speed") @Bounds(min = 1, max = 15) public int speed = 10;
    @Value(value = "Toggle") public boolean toggle = true;
    boolean motion;
    private Timer timer = new Timer();

    @Override
    public void onToggle(){
        motion = false;
        timer.reset();
        Main.TICK_TIMER = 1;
    }

    @Override
    public void onTick(){
        if (nullCheck()) return;

        double ss[] = EntityUtil.forward(this.speed / 1.5);

        if (mc.player.onGround && !motion && EntityUtil.INSTANCE.isMoving(mc.player)) {
            mc.player.jump();
            motion = true;
            timer.reset();
        }

        if (motion) {
            if (timer.passed(70)) {
                mc.player.motionX = ss[0];
                mc.player.motionZ = ss[1];
                motion = false;
                timer.reset();
                if (toggle) setToggled(false);
            }
        }
    }

    @Handler
    public Listener<MoveEvent> moveListener = new Listener<>(MoveEvent.class, event -> {
        if (!mc.player.onGround){
            event.motionY -= 0.08f;
        }
    });
}
