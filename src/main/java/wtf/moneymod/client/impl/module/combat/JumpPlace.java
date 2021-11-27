package wtf.moneymod.client.impl.module.combat;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.client.impl.utility.impl.world.BlockUtil;

@Module.Register( label = "JumpPlace", cat = Module.Category.COMBAT)
public class JumpPlace extends Module {

    private BlockPos pos;
    int old;
    @Override
    public void onEnable(){
        old = mc.player.inventory.currentItem;
        pos = new BlockPos(mc.player.getPositionVector());
    }

    @Override
    public void onDisable(){
        pos = null;
    }

    @Override
    public void onTick(){
        if (nullCheck()) return;
        int slot = ItemUtil.findItem(Blocks.OBSIDIAN, Blocks.ENDER_CHEST);
        if (slot == -1) setToggled(false);
        if (mc.player.onGround) mc.player.jump();
        ItemUtil.swapToHotbarSlot(slot, false);
        BlockUtil.INSTANCE.placeBlock(pos);
        ItemUtil.swapToHotbarSlot(old, false);
        if (mc.world.getBlockState(pos).getBlock() != Blocks.AIR) setToggled(false);
    }

}
