package wtf.moneymod.client.impl.module.combat;

import net.minecraft.util.math.BlockPos;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.world.BlockUtil;
import wtf.moneymod.client.mixin.mixins.ducks.AccessorKeyBinding;

@Module.Register( label = "BurrowBypass", cat = Module.Category.COMBAT)
public class SelfFillBypass extends Module {

    /**
    @author: PigHax
    client: moneymod rewrite
    date: Nov 4, 2021
     */


    BlockPos position;
    int time;
    BlockPos pos;
    int stages;
    int delay, pdelay,stage,jumpdelay,toggledelay;
    boolean jump;
    Timer timer = new Timer();

    @Override public void onEnable(){
        position = new BlockPos(mc.player.getPositionVector());
    }
    @Override public void onToggle(){

        time = 0;
        pos = null;
        stages = 0;

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
        if (stage == 1) {
            delay++;
            ((AccessorKeyBinding) mc.gameSettings.keyBindJump).setPressed(true);
            Main.TICK_TIMER = 30;
            if (delay >= 42) {
                stage = 2;
                delay = 0;
                Main.TICK_TIMER = 1;
                ((AccessorKeyBinding) mc.gameSettings.keyBindJump).setPressed(false);
            }
        }
        if (stage == 2){
            Main.TICK_TIMER = 1;
            if (mc.player.onGround) ((AccessorKeyBinding) mc.gameSettings.keyBindJump).setPressed(true);;
            BlockUtil.INSTANCE.placeBlock(position);
            pdelay++;
            if (pdelay >= 30){
                stage = 3;
                pdelay = 0;
                ((AccessorKeyBinding) mc.gameSettings.keyBindJump).setPressed(false);
                Main.TICK_TIMER = 1;

            }
        }
        if (stage == 3){
            toggledelay++;
            Main.TICK_TIMER = 30;
            ((AccessorKeyBinding) mc.gameSettings.keyBindJump).setPressed(true);
            if (toggledelay >= 25) {
                mc.player.motionY -= 0.4;
                Main.TICK_TIMER = 1;
                ((AccessorKeyBinding) mc.gameSettings.keyBindJump).setPressed(false);
                setToggled(false);
            }
        }
    }
}