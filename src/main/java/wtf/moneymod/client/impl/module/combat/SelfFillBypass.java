package wtf.moneymod.client.impl.module.combat;

import net.minecraft.block.BlockEnderChest;
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


    boolean doBurrow = false;
    BlockPos position;
    @Override public void onEnable(){
        position = new BlockPos(mc.player.getPositionVector());
        doBurrow = true;
    }

    @Override public void onToggle(){
        position = null;
        doBurrow = false;
    }

    @Override public void onTick() {
        if (position != null && !nullCheck()){

            if (doBurrow) {
                if (mc.player.onGround) mc.player.motionY = 0.41999998688697815D;
                BlockUtil.INSTANCE.placeBlock(position);
                if (mc.world.getBlockState(position).getBlock() instanceof BlockEnderChest) {
                    setToggled(false);
                }
            }
        }
    }
}
