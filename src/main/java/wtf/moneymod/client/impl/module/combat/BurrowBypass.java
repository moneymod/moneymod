package wtf.moneymod.client.impl.module.combat;

import net.minecraft.block.BlockObsidian;
import net.minecraft.util.math.BlockPos;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.client.impl.utility.impl.world.BlockUtil;

@Module.Register( label = "SelfFillBypass", cat = Module.Category.COMBAT )
public class BurrowBypass extends Module {

    /**
     * @Author: PigHax
     * date: 18.11.2021
     */

    @Value(value = "Ticks") @Bounds(min = 8, max = 130) public int ticks = 250;
    @Value(value = "Timer") @Bounds(max = 100) public int timer = 20;
    @Value(value = "Auto Swap") public boolean autoSwap = false;
    @Value(value = "Jump Mode") public JumpMode jumpMode = JumpMode.NORMAL;
    public enum JumpMode{
        NORMAL, MODIFY
    }

    private int delay;
    private BlockPos startPos;
    @Override
    public void onEnable(){
        startPos = new BlockPos(mc.player.getPositionVector());
        delay = 0;
    }
    @Override
    public void onDisable(){
        Main.TICK_TIMER = 1;
        startPos = null;
    }

    @Override
    public void onTick() {
        if (nullCheck()) return;
        if (ItemUtil.findItem(BlockObsidian.class) == -1) setToggled(false);
        delay++;
        if (mc.player.onGround) mc.player.motionY = 0.48f;
        Main.TICK_TIMER = timer;
        if (delay >= (ticks / 2)){
            int old = mc.player.inventory.currentItem;
            if (autoSwap) ItemUtil.swapToHotbarSlot(ItemUtil.findItem(BlockObsidian.class), false);
            BlockUtil.INSTANCE.placeBlock(startPos);
            if (autoSwap) ItemUtil.swapToHotbarSlot(old, false);
        }
        if (delay >= ticks){
            Main.TICK_TIMER = 1;
            delay = 0;
            setToggled(false);
        }

    }
}
