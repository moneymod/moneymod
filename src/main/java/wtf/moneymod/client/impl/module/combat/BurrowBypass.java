package wtf.moneymod.client.impl.module.combat;

import net.minecraft.block.BlockObsidian;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.world.BlockUtil;
import wtf.moneymod.client.mixin.mixins.ducks.AccessorKeyBinding;

@Module.Register( label = "BurrowBypass", cat = Module.Category.COMBAT )
public class BurrowBypass extends Module {

    /**
     * @Author: PigHax
     * date: 18.11.2021
     */

    @Value(value = "Ticks") @Bounds(min = 8, max = 128) public int ticks = 128;
    @Value(value = "Timer") @Bounds(max = 100) public int timer = 20;
    @Value(value = "Jump Mode") public JumpMode jumpMode = JumpMode.NORMAL;
    public enum JumpMode{
        NORMAL, MODIFY
    }

    private int delay;
    private BlockPos startPos;
    @Override
    public void onEnable(){
        startPos = new BlockPos(mc.player.getPositionVector());
    }


    @Override
    public void onToggle(){
        startPos = null;
        delay = 0;
        Main.TICK_TIMER = 1;
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;

        delay++;
        BlockUtil.INSTANCE.placeBlock(startPos);
        Main.TICK_TIMER = timer;
        if (delay >= ticks){
            Main.TICK_TIMER = 1;
            delay = 0;
            setToggled(false);
        }
        if (mc.player.onGround){
            switch (jumpMode){
                case NORMAL:
                    mc.player.motionY = 0.4;
                    break;
                case MODIFY:
                    mc.player.motionY = 0.46;
                    mc.player.motionY = -0.05;
                    break;
            }
        }
    }
}
