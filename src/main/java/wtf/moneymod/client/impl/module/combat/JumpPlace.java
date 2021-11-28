package wtf.moneymod.client.impl.module.combat;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import wtf.moneymod.client.Main;
import wtf.moneymod.client.api.setting.annotatable.Value;
import wtf.moneymod.client.impl.module.Module;
import wtf.moneymod.client.impl.utility.impl.player.ItemUtil;
import wtf.moneymod.client.impl.utility.impl.world.BlockUtil;

@Module.Register( label = "JumpPlace", cat = Module.Category.COMBAT)
public class JumpPlace extends Module {

    @Value(value = "Rotate") public boolean rotate = false;
    @Value(value = "Packet") public boolean packet = false;

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
        if (ItemUtil.findItem(Blocks.OBSIDIAN) == -1){
            setToggled(false);
            return;
        } else {
            int slot = ItemUtil.findItem(Blocks.OBSIDIAN);
            if (mc.player.onGround) mc.player.jump();
            if (rotate) Main.getMain().getRotationManagement().look(pos,packet);
            ItemUtil.swapToHotbarSlot(slot, false);
            BlockUtil.INSTANCE.placeBlock(pos);
            ItemUtil.swapToHotbarSlot(old, false);
            if (mc.world.getBlockState(pos).getBlock() != Blocks.AIR) setToggled(false);
        }
    }
}
