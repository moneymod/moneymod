package wtf.moneymod.client.impl.module.combat;

import net.minecraft.block.BlockObsidian;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.client.impl.utility.impl.world.BlockUtil;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;

@Module.Register( label = "BurrowBypass", cat = Module.Category.COMBAT)
public class SelfFillBypass extends Module {

    @Value(value = "Bypass Method") public Mode mode = Mode.PIGBYPASS;
    @Value(value = "Ticks") @Bounds( min = 10, max = 100) public int ticks = 50;
    @Value(value = "Toggle Delay") @Bounds( min = 10, max = 60) public int toggleDelays = 20;
    @Value(value = "One Delay") @Bounds( min = 10, max = 60) public int oneDelays = 42;
    @Value(value = "Second Delay") @Bounds( min = 10, max = 60) public int placeDelay = 30;



    BlockPos position;
    int delay, pdelay,stage,jumpdelay,toggledelay;
    boolean jump;
    Timer timer = new Timer();
    @Override public void onEnable(){
        position = new BlockPos(mc.player.getPositionVector());
    }
    @Override public void onToggle(){
        pdelay = 0;
        stage = 1;
        toggledelay = 0;
        jumpdelay = 0;
        timer.reset();
        jump = false;
        Main.TICK_TIMER = 1;
        position = null;
        delay = 0;
    }

    @Override public void onTick() {

        if (position != null) {
            if(mode == Mode.PIGBYPASS){
                firstmethod();
            } else {
            }
        }
    }

    public void firstmethod(){
        if (stage == 1) {
            delay++;
            if (mc.player.onGround) mc.player.jump();
            Main.TICK_TIMER = ticks;
            if (delay >= oneDelays) {
                stage = 2;
                delay = 0;
                Main.TICK_TIMER = 1;
                jump = true;
            }
        }
        if (stage == 2){
            Main.TICK_TIMER = 1;
            if (mc.player.onGround) mc.player.jump();
            BlockUtil.INSTANCE.placeBlock(position);
            pdelay++;
            if (pdelay >= placeDelay){
                stage = 3;
                pdelay = 0;
                Main.TICK_TIMER = 1;

            }
        }
        if (stage == 3){
            toggledelay++;
            Main.TICK_TIMER = ticks;
            if (mc.player.onGround) mc.player.jump();
            if (toggledelay >= toggleDelays) {
                mc.player.motionY -= 0.4;
                Main.TICK_TIMER = 1;
                setToggled(false);
            }
        }
    }
    public enum Mode{
        PIGBYPASS, SECONDBYPASS
    }
}