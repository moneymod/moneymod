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

    @Value(value = "Height") @Bounds(min = -8, max = 8) public int height = 4;

    private BlockPos startPos;
    int delay = 0;
    int stage = 0;
    boolean jumping = false;
    @Override
    public void onEnable(){
        stage = 1;
        startPos = new BlockPos(mc.player.getPositionVector());
    }

    @Override
    public void onToggle(){
        startPos = null;
        delay = 0;
        Main.TICK_TIMER = 1;
        jump(false);

    }

    public void jump(boolean bo){
        ((AccessorKeyBinding) mc.gameSettings.keyBindJump).setPressed(bo);
    }

    @Override
    public void onTick() {
        if (stage == 1){
            delay++;
            Main.TICK_TIMER = 5.5F;
            jump(true);
            if (delay >= 80){
                jump(false);
                Main.TICK_TIMER = 1;
                delay = 0;
                mc.player.motionY -= 0.4F;
                stage = 2;
                jumping = true;
            }
        }
        if (stage == 2){
            delay++;
            Main.TICK_TIMER = 10.5F;
            if (jumping){
                mc.player.motionY+= 0.479999;
                jumping = false;
            }
            BlockUtil.INSTANCE.placeBlock(startPos);
            if (mc.world.getBlockState(startPos).getBlock() instanceof BlockObsidian){
                jump(false);
                Main.TICK_TIMER = 1;
                delay = 0;
                stage = 0;
                setToggled(false);
            }
            if (delay >= 60 && mc.world.getBlockState(startPos).getBlock() != Blocks.OBSIDIAN){
                jump(false);
                Main.TICK_TIMER = 1;
                delay = 0;
                stage = 0;
                setToggled(false);
            }
        }
    }
    
}
