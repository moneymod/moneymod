package wtf.moneymod.client.impl.module.movement;

import net.minecraft.entity.player.EntityPlayer;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;

@Module.Register( label = "Warp", cat = Module.Category.MOVEMENT)
public class Warp extends Module {

    //Pro module

    @Value(value = "Time") @Bounds(min = 1, max = 16) public int time = 10;
    @Value(value = "Tick") @Bounds(min = 1, max = 8) public double tick = 4;
    @Value(value = "Mode") public Mode mode = Mode.TIMER;
    @Value(value = "Step") public boolean step = true;
    @Value(value = "OnlyMoving") public boolean onlyMoving = true;
    //pov when you not pasting ^

    int delay;

    @Override public void onToggle(){
        Main.TICK_TIMER = 1;
        delay = 0;
        mc.player.stepHeight = 0.6f;
    }
    @Override public void onTick() {
        if (onlyMoving) if (EntityUtil.INSTANCE.isMoving(mc.player)){} else {
            Main.TICK_TIMER = 1;
            setToggled(false);
        }
        if (step) mc.player.stepHeight = 2;
        if (mode == Mode.TIMER) {
            delay++;
            Main.TICK_TIMER = (float) tick;
            if (delay >= time) {
                delay = 0;
                setToggled(false);
            }
        }

        if (mode == Mode.ALWAYS){
            Main.TICK_TIMER = ( float ) tick;
        }
    }
    public enum Mode {
        TIMER, ALWAYS
    }
}
