package wtf.moneymod.client.impl.module.combat;

import net.minecraft.block.BlockObsidian;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.misc.Timer;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.client.impl.utility.impl.world.BlockUtil;
import wtf.moneymod.client.impl.utility.impl.world.ChatUtil;
import wtf.moneymod.client.impl.utility.impl.world.EntityUtil;

@Module.Register( label = "BurrowBypass", cat = Module.Category.COMBAT)
public class SelfFillBypass extends Module {

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
            //SHIT CODE MOMENT
            if (stage == 1) {
                ChatUtil.INSTANCE.sendMessage("stage = 1");
                delay++;
                if (mc.player.onGround) mc.player.jump();
                Main.TICK_TIMER = 4;
                if (delay >= 42) {
                    stage = 2;
                    delay = 0;
                    Main.TICK_TIMER = 1;
                    jump = true;
                }
            }
            if (stage == 2){
                Main.TICK_TIMER = 1;
                ChatUtil.INSTANCE.sendMessage("stage = 2");
                if (mc.player.onGround) mc.player.jump();
                BlockUtil.INSTANCE.placeBlock(position);

                pdelay++;
                if (pdelay >= 28){
                    stage = 3;
                    pdelay = 0;
                    Main.TICK_TIMER = 1;

                }
            }
            if (stage == 3){
                ChatUtil.INSTANCE.sendMessage("stage = 3");
                toggledelay++;
                Main.TICK_TIMER = 8;
                if (mc.player.onGround) mc.player.jump();
                if (toggledelay >= 30) {
                    mc.player.motionY -= 0.4;
                    Main.TICK_TIMER = 1;
                    setToggled(false);
                }
            }
        }
    }
}
