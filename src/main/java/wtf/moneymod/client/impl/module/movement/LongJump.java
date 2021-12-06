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

    @Value(value = "Speed") @Bounds(min = 0.1f, max = 12) public float speed = 10;
    @Value(value = "Toggle") public boolean toggle = true;
    @Value(value = "MiniJump") public boolean miniJump = true;
    @Value(value = "Cancel Position") public boolean cancelPosition = true;

    boolean motion;
    int stage = 0;
    private Timer timer = new Timer();

    @Override
    public void onToggle(){
        stage = 0;
        motion = false;
        timer.reset();
        Main.TICK_TIMER = 1;
    }

    @Override
    public void onTick(){
        if (nullCheck()) return;
        double ss[] = EntityUtil.forward(this.speed / 2);

        if (mc.player.onGround && EntityUtil.INSTANCE.isMoving(mc.player)){
            if (!motion){
                mc.player.motionY = miniJump ? 0.25 : 0.4;
            }
            timer.reset();
            motion = true;
        }
        if (!mc.player.onGround){
            if (motion && timer.passed(miniJump ? 4 : 40)){
                stage = 1;
                mc.player.motionX = ss[0];
                mc.player.motionZ = ss[1];
                timer.reset();
                motion = false;
            }
        } else {
            if (stage == 1){
                if (cancelPosition) {
                    mc.player.motionX = 0;
                    mc.player.motionZ = 0;
                }
                if (toggle){
                    mc.player.motionY = 0;
                    setToggled(false);
                }
                stage = 0;
            }
        }

    }

    @Handler
    public Listener<MoveEvent> moveListener = new Listener<>(MoveEvent.class, event -> {
        if (!mc.player.onGround && motion){
            event.motionY -= 0.05f;
        }
    });
}
