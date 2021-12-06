package wtf.moneymod.client.impl.module.movement;

import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;

@Module.Register( label = "HighJump", cat = Module.Category.MOVEMENT)
public class HighJump extends Module {

    @Value(value = "Mode") public Mode mode = Mode.DEFAULT;
    @Value(value = "Speed") @Bounds(min = 0.1f, max = 15f) public float speed = 5f;
    public enum Mode{
        DEFAULT, ONLYWATER,
    }

    private Timer timer = new Timer();

    @Override
    public void onToggle(){
        timer.reset();
    }

    @Override
    public void onTick(){
        if (nullCheck()) return;
        if (mc.player.onGround && mc.player.motionY < 0.0){
            if (mode == Mode.ONLYWATER) if (!mc.player.isInWater()) return;
            mc.player.jump();
            mc.player.jumpMovementFactor = 0.555f;
            if (timer.passed(7)){
                mc.player.motionY = speed;
                timer.reset();
                setToggled(false);
            }
        }
    }
}
