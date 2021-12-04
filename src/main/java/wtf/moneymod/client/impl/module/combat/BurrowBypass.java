package wtf.moneymod.client.impl.module.combat;

import net.minecraft.block.BlockObsidian;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.annotatable.Bounds;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.client.impl.utility.impl.world.BlockUtil;

@Module.Register( label = "BurrowBypass", cat = Module.Category.COMBAT )
public class BurrowBypass extends Module {

    /**
     * @Author: PigHax
     * date: 18.11.2021
     */

    @Value("Timer") @Bounds(min = 1, max = 100) public int timertick = 50;

    int delay = 0;
    int delay2 = 0;
    int stage = 1;
    int old = 0;
    BlockPos pos;

    @Override
    public void onToggle(){
        delay = 0;
        delay2 = 0;
        Main.TICK_TIMER = 1;
    }
    @Override
    public void onEnable(){
        Main.TICK_TIMER = 1;
        delay = 0;
        delay2 = 0;
        old = mc.player.inventory.currentItem;
        stage = 1;
        pos = new BlockPos(mc.player.getPositionVector());
    }

    @Override
    public void onTick(){
        if (nullCheck()) return;
        System.out.println("OnTick");
        delay++;
        if (stage == 1){
            System.out.println("Stage1");


            Main.TICK_TIMER = 50;
            if (mc.player.onGround){
                mc.player.jump();
                mc.player.motionY -= 0.25;
            }
            if (delay >= 30){
                Main.TICK_TIMER = 1;
                delay = 0;
                stage = 1;
            }
        }
        if (stage == 2){

            System.out.println("Stage2");
            if (nullCheck()) return;
            if (ItemUtil.findItem(Blocks.OBSIDIAN) == -1){
                setToggled(false);
                return;
            } else {
                int slot = ItemUtil.findItem(Blocks.OBSIDIAN);
                if (mc.player.onGround) mc.player.jump();
                ItemUtil.swapToHotbarSlot(slot, false);
                BlockUtil.INSTANCE.placeBlock(pos);
                ItemUtil.swapToHotbarSlot(old, false);
                if (mc.world.getBlockState(pos).getBlock() != Blocks.AIR) stage = 2;
            }
        }
        if (stage == 3){

            System.out.println("Stage3");
            delay2++;
            Main.TICK_TIMER = timertick;
            if (delay2 >= 30){
                Main.TICK_TIMER = 1;
                delay2 = 0;
                setToggled(false);
            }
            if (mc.player.onGround){
                mc.player.jump();
                mc.player.motionY -= 0.25;
            }
        }
    }
}
